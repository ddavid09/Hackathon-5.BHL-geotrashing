package bhl.geotrashing.app.firestore

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import bhl.geotrashing.app.LoadingActivity
import bhl.geotrashing.app.MainActivity
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File

class DataBase(val contex: Context) {
    val db = Firebase.firestore
    val user = FirebaseAuth.getInstance().currentUser
    val TAG = "DataBase"
    val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun uploadTrash( location: LatLng,description: String, bitmap: Bitmap) {
        val loadingIntent = Intent(contex, LoadingActivity::class.java)
        val startIntent = Intent(contex, MainActivity::class.java)
        val locationGeoPoint = GeoPoint(location.latitude, location.longitude)
        val storageRef = storage.reference
        val ref = db.collection("trash").document()
        val trash = Trash(locationGeoPoint, user?.uid!!, description, ref.id)
        contex.startActivity(loadingIntent)
        ref.set(trash)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${ref.id}")
                    val pictureRef = storageRef.child("trash/" + ref.id + ".jpg")
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                    val data = baos.toByteArray()
                    var uploadTask = pictureRef.putBytes(data)
                    uploadTask.addOnSuccessListener { documentReference ->
                        contex.startActivity(startIntent)
                        Log.d(TAG, "Picture send")
                        val toast = Toast.makeText(
                                this.contex,
                                "Poprawnie dodano zgłoszenie",
                                Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }.addOnFailureListener { e ->
                        contex.startActivity(startIntent)
                        Log.w(TAG, "Error sending picture", e)
                        val toast = Toast.makeText(
                                this.contex,
                                "Niepoprawnie dodano zgłoszenie",
                                Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
    }

    fun getAllTrash(collected: Boolean, confirmed: Boolean): MutableLiveData<ArrayList<Trash>>  {
        val liveDataTrashList: MutableLiveData<ArrayList<Trash>> = MutableLiveData()
        if(!collected && confirmed){
            Log.w(TAG, "ERROR ERROR ERROR ERROR ERROR ERROR")
        }
        db.collection("trash")
                .whereEqualTo("collected", collected).whereEqualTo("confirmed", confirmed).addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val trashList = ArrayList<Trash>()
                    for (doc in value!!) {
                        doc.toObject(Trash::class.java).let {
                            trashList.add(it)
                        }
                    }
                    Log.d(TAG, "Current trashes: $trashList")
                    liveDataTrashList.value=trashList
                }
        return liveDataTrashList
    }

    fun getTrashPhoto(trash: Trash, collected: Boolean = false): MutableLiveData<Bitmap> {
        val liveBitmap: MutableLiveData<Bitmap> = MutableLiveData()
        val storageRef = storage.reference

        var pictureRef = storageRef.child("trash/"+ trash.ID + ".jpg")
        if(collected) pictureRef = storageRef.child("collected_trash/" + trash.ID + ".jpg")

        val localFile = File.createTempFile("images", "jpg")

        pictureRef.getFile(localFile).addOnSuccessListener {
            liveBitmap.value = BitmapFactory.decodeFile(localFile.absolutePath)
            // Local temp file has been created
        }.addOnFailureListener {
            // Handle any errors
        }
        return liveBitmap
    }


    fun uploadTrashCollecting(bitmap: Bitmap, trash: Trash){
        val storageRef = storage.reference
        trash.collected=true
        trash.collectorID = user?.uid!!
        db.collection("trash").document(trash.ID).set(trash)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${trash.ID}")
                    val pictureRef = storageRef.child("collected_trash/" + trash.ID + ".jpg")
                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                    val data = baos.toByteArray()
                    var uploadTask = pictureRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        // Handle unsuccessful uploads
                    }.addOnSuccessListener { documentReference ->
                        Log.d(TAG, "Picture send")
                        val toast = Toast.makeText(
                                this.contex,
                                "Poprawnie dodano zgłoszenie",
                                Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }.addOnFailureListener { e ->
                        Log.w(TAG, "Error sending picture", e)
                        val toast = Toast.makeText(
                                this.contex,
                                "Niepoprawnie dodano zgłoszenie",
                                Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

    }

    fun getTrashFromLatLng(location: LatLng): MutableLiveData<Trash> {
        val locationGeoPoint = GeoPoint(location.latitude, location.longitude)
        val MLtrash :MutableLiveData<Trash> = MutableLiveData()
        db.collection("trash")
                .whereEqualTo("locationGeoPoint", locationGeoPoint).addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    for (doc in value!!) {
                        MLtrash.value=doc.toObject(Trash::class.java)
                        break
                    }
                }
        return MLtrash

    }

    fun confirmTrashCollecting(trash: Trash){
        val storageRef = storage.reference
        trash.confirmed=true
        trash.confirmatorID= user!!.uid
        db.collection("trash").document(trash.ID).set(trash)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${trash.ID}")
//                Toast.makeText(
//                    this.contex,
//                    "Poprawnie potwierdzono zebranie smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }
                .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//                Toast.makeText(
//                    this.contex,
//                    "Błąd w potwierdzaniu zebrania smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }

    }

    fun setUserNickname(nickname: String){
        user?.uid?.let {
            val userData = User(it,nickname)
            db.collection("users").document(it).set(userData)
                    .addOnSuccessListener {
                        Log.d(TAG, "Poprawnie dodano nick użytkownika: ${it}")
//                    Toast.makeText(
//                        this.contex,
//                        "Poprawnie dodano nick użytkownika",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error adding document", e)
//                    Toast.makeText(
//                        this.contex,
//                        "Błąd w dodawaniu nicka użytkownika",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    }
        }
    }

    fun genPointsFor(user: User,mutableLiveData: MutableLiveData<HashMap<String,User>>) {
        db.collection("trash")
                .whereEqualTo("collectorID",user.userID)
                .whereEqualTo("collected",true)
                .whereEqualTo("confirmed",true)
                .get()
                .addOnSuccessListener{ documents ->
                    val hashMap = mutableLiveData.value
                    var new_points:Int = 0
                    for (document in documents) {
                        new_points+=1000

                        Log.d(TAG, "${document.id} => ${document.data}")

                    }
                    hashMap?.get(user.userID)!!.points+=new_points
                    mutableLiveData.value=hashMap
                    Log.w(TAG, user.nickname+" points "+mutableLiveData.value!!.get(user.userID)!!.points)

                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
//                Toast.makeText(
//                    this.contex,
//                    "Błąd w potwierdzaniu zebrania smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }
        db.collection("trash")
                .whereEqualTo("confirmatorID",user.userID)
                .whereEqualTo("collected",true)
                .whereEqualTo("confirmed",true)
                .get()
                .addOnSuccessListener{ documents ->
                    val hashMap = mutableLiveData.value
                    var new_points:Int = 0
                    for (document in documents) {
                        new_points+=50

                        Log.d(TAG, "${document.id} => ${document.data}")

                    }
                    hashMap?.get(user.userID)!!.points+=new_points
                    mutableLiveData.value=hashMap
                    Log.w(TAG, user.nickname+" points "+mutableLiveData.value!!.get(user.userID)!!.points)

                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
//                Toast.makeText(
//                    this.contex,
//                    "Błąd w potwierdzaniu zebrania smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }
        db.collection("trash")
                .whereEqualTo("creatorID",user.userID)
                .get()
                .addOnSuccessListener{ documents ->
                    val hashMap = mutableLiveData.value
                    var new_points:Int = 0
                    for (document in documents) {
                        new_points+=100

                        Log.d(TAG, "${document.id} => ${document.data}")

                    }
                    hashMap?.get(user.userID)!!.points+=new_points
                    mutableLiveData.value=hashMap
                    Log.w(TAG, user.nickname+" points "+mutableLiveData.value!!.get(user.userID)!!.points)
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
//                Toast.makeText(
//                    this.contex,
//                    "Błąd w potwierdzaniu zebrania smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }
    }

    fun  getRanking(): MutableLiveData<HashMap<String, User>> {
        val rankingLD:MutableLiveData<HashMap<String,User>> = MutableLiveData<HashMap<String,User>>()
        rankingLD.value=HashMap()
        db.collection("users").addSnapshotListener { documents, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            for (doc in documents!!) {
                doc.toObject(User::class.java).let {
                    val hashMap =  rankingLD.value
                    hashMap?.put(it.userID, it)
                    rankingLD.value=hashMap
                    genPointsFor(it,rankingLD)
                    Log.w(TAG, "User "+it)
                }
            }
        }
        return rankingLD
    }

    fun getTrashToConfirm(): MutableLiveData<Trash> {
        val MLDTrash:MutableLiveData<Trash> = MutableLiveData<Trash>()
        db.collection("trash")
                .whereEqualTo("collected", true).whereEqualTo("confirmed",false).addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    for (doc in value!!) {
                        MLDTrash.value=doc.toObject(Trash::class.java)
                        break
                    }
                }
        return MLDTrash
    }

    fun unconfirmTrashCollecting(trash: Trash){
        val storageRef = storage.reference
        trash.collected=false
        db.collection("trash").document(trash.ID).set(trash)
                .addOnSuccessListener {
                    Log.d(TAG, "DocumentSnapshot added with ID: ${trash.ID}")
//                Toast.makeText(
//                    this.contex,
//                    "Poprawnie potwierdzono zebranie smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
//                Toast.makeText(
//                    this.contex,
//                    "Błąd w potwierdzaniu zebrania smieci",
//                    Toast.LENGTH_SHORT
//                ).show()
                }

    }









}



