package bhl.geotrashing.app.firestore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.GeoPoint

class DataBase() {
    val db = Firebase.firestore
    val TAG = "DataBase"

    fun uploadTrash(
        location: LatLng,
        creatorID: Int,
        pictureID: Int,
        description: String,
        collected: Boolean = false
    ) {
        val locationGeoPoint = GeoPoint(location.latitude, location.longitude)
        val trash = Trash(locationGeoPoint,creatorID,pictureID,description,collected)

        db.collection("trash")
            .add(trash)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    fun getAllTrash(liveDataTrashList: MutableLiveData<ArrayList<Trash>> = MutableLiveData()): MutableLiveData<ArrayList<Trash>> {
        var trashList = ArrayList<Trash>()
        Log.d(TAG, "getAllSubjecList()")
        db.collection("trash")
            .whereEqualTo("collected",false)
            .get().addOnSuccessListener {
                    documents ->
                for (document in documents) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    trashList.add(document.toObject(Trash::class.java))
                    liveDataTrashList.value=trashList
                }
                Log.d(TAG,"Data change in ${liveDataTrashList.value}")

            }
        return liveDataTrashList
    }

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




