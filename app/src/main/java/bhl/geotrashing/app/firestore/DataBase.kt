package bhl.geotrashing.app.firestore

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
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

    fun uploadTrash(
        location: LatLng,
        description: String,
        bitmap: Bitmap
    ) {

        val locationGeoPoint = GeoPoint(location.latitude, location.longitude)
        val storageRef = storage.reference

        val ref = db.collection("trash").document()
        val trash = Trash(locationGeoPoint, user?.uid!!, description, ref.id)
        ref.set(trash)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${ref.id}")
                val pictureRef = storageRef.child("trash/" + ref.id + ".jpg")
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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
        db.collection("trash").document(trash.ID).set(trash)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with ID: ${trash.ID}")
                val pictureRef = storageRef.child("collected_trash/" + trash.ID + ".jpg")
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
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





}




