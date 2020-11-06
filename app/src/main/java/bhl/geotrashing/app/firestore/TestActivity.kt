package bhl.geotrashing.app.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*

class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val dataBase = DataBase(this)
        val rnd = Random()

        val trashLiveData = dataBase.getAllTrash()
        trashLiveData.observe(this, Observer {
            Log.d(TAG,"Data change in ${it}")
        })

//        test_button.setOnClickListener {
//
//            dataBase.uploadTrash(
//                LatLng(1.0,1.0),"description",
//
//        }


    }

}