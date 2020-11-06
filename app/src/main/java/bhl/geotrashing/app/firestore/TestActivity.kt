package bhl.geotrashing.app.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {
    val TAG = "TestActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val dataBase = DataBase()

        test_button.setOnClickListener {

            dataBase.uploadTrash(
                LatLng(1.0,1.0),
                1,
                1,
                "Smieci 1")
            dataBase.uploadTrash(
                LatLng(1.0,1.0),
                1,
                1,
                "Smieci 2")
            dataBase.uploadTrash(
                LatLng(1.0,1.0),
                3,
                4,
                "Smieci 3")
            dataBase.uploadTrash(
                LatLng(1.0,1.0),
                1,
                1,
                "Smieci 4",true)
            dataBase.uploadTrash(
                LatLng(1.0,1.0),
                2,
                1,
                "Smieci 5")

            val trashLiveData = dataBase.getAllTrash()

            trashLiveData.observe(this, Observer {
                Log.d(TAG,"Data change in ${it}")
            })
        }


    }

}