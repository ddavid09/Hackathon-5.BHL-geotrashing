package bhl.geotrashing.app.firestore

import android.content.Context
import android.graphics.Bitmap
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
        // Create a storage reference from our app
        val storageRef = storage.reference
        // Create a reference to "mountains.jpg"

        val trash = Trash(locationGeoPoint, user?.uid!!, description, false)

        db.collection("trash")
            .add(trash)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                val pictureRef = storageRef.child("images/"+documentReference.id)
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                var uploadTask = pictureRef.putBytes(data)
                uploadTask.addOnFailureListener {
                    // Handle unsuccessful uploads
                }.addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Picture send")
                    val toast = Toast.makeText(this.contex, "Poprawnie dodano zgłoszenie", Toast.LENGTH_SHORT)
                    toast.show()
                }.addOnFailureListener { e ->
                    Log.w(TAG, "Error sending picture", e)
                    val toast = Toast.makeText(this.contex, "Niepoprawnie dodano zgłoszenie", Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getAllTrash(liveDataTrashList: MutableLiveData<ArrayList<Trash>> = MutableLiveData()): MutableLiveData<ArrayList<Trash>> {
        Log.d(TAG, "getAllSubjecList()")
        db.collection("trash")
            .whereEqualTo("collected", false).addSnapshotListener { value, e ->
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
                Log.d(TAG, "Current trashs: $trashList")
                liveDataTrashList.value=trashList
            }
        return liveDataTrashList
    }

    fun uploadBitmapa(bitmap: Bitmap, imageName: String){

    }

//    Log.d("Data change", " db.collection(\"list\").whereEqualTo(\"teacherID\",teacherID")
//    var allAttendanceList = ArrayList<AttendenceList>()
//    liveDataAllAttendenceList.value=allAttendanceList
//    for (doc in documents) {
//        var newAttendenceList = doc.toObject(AttendenceList::class.java)
//        doc.reference.collection("attendence").addSnapshotListener{attendenceList, e ->
//            Log.d("Data change", " doc.reference.collection(\"attendence\").")
//            if (e != null) {
//                Log.w(TAG, "Listen failed.", e)
//                return@addSnapshotListener
//            }
//            newAttendenceList.attendence=ArrayList()
//            if (attendenceList != null) {
//                for(doc2 in attendenceList){
//
//                    newAttendenceList.attendence.add(doc2.toObject(Attendence::class.java))
//                    Log.d(TAG,newAttendenceList.attendence.toString()+" "+doc2)
//                }
//            }
//
//            allAttendanceList.removeIf{ it.code==newAttendenceList.code }
//            allAttendanceList.add(newAttendenceList)
//            liveDataAllAttendenceList.value=allAttendanceList
//        }


//    fun getAllTrash(){
//
//        db.collection("trash")
//            .whereEqualTo("collected", false)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
//            }
//    }



}




