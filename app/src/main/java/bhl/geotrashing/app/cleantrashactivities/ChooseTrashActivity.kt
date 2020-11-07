package bhl.geotrashing.app.cleantrashactivities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import bhl.geotrashing.app.firestore.DataBase
import kotlinx.android.synthetic.main.activity_choose_trash.*


class ChooseTrashActivity : AppCompatActivity() {

    lateinit var confirmTrashIntent: Intent
    var btnClickable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_trash)
        confirmTrashIntent = Intent(this, ConfirmTrashActivity::class.java)

       /* intentToNextActivity.putExtra("long", trash?.locationGeoPoint?.longitude)
        intentToNextActivity.putExtra("lat", trash?.locationGeoPoint?.latitude)*/
        val intent : Intent = getIntent()
        val locationLong = intent.getDoubleExtra("long", 0.0)
        val locationLat = intent.getDoubleExtra("lat", 0.0)
        val db = DataBase(this)
        val location = com.google.android.gms.maps.model.LatLng(locationLat, locationLong)
        Log.d("ChooseTrashActivity", location.toString())

        val MLtrash = db.getTrashFromLatLng(location)
        /*val LDpicture = DataBase(this).getTrashPhoto(trash)
        ChooseTrashActivityTextViewId.setText(trash.description)*/

        MLtrash.observe(this, Observer {
            //Log.d("ChooseTrashActivity", "inObserve")
            if (it != null) {

                ChooseTrashActivityTextViewId.setText(it.description)
                val MLPicture = db.getTrashPhoto(it, collected = false)
                MLPicture.observe(this, Observer {
                    if (it != null) {
                        ChooseTrashActivityImageViewId.setImageBitmap(it)
                        btnClickable = true
                    } else{
                        Toast.makeText(this, "Nie można pobrać zdjęcia", Toast.LENGTH_SHORT).show()
                    }
                })

            } else{
                //Log.d("ChooseTrashActivity", "out")
                Toast.makeText(this, "Nie można pobrać informacji o śmieciach", Toast.LENGTH_SHORT).show()
            }

        })

        ChooseTrashActivityButtonId.setOnClickListener {
            if (btnClickable){
                confirmTrashIntent.putExtra("long", locationLong)
                confirmTrashIntent.putExtra("lat", locationLat)
                startActivity(this.confirmTrashIntent)
            } else{
                Toast.makeText(this, "Prosze poczekać na pobranie zdjęcia", Toast.LENGTH_SHORT).show()
            }

        }


    }


}