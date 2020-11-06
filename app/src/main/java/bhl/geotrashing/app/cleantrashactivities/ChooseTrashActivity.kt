package bhl.geotrashing.app.cleantrashactivities

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import bhl.geotrashing.app.firestore.DataBase
import bhl.geotrashing.app.firestore.Trash
import kotlinx.android.synthetic.main.activity_choose_trash.*


class ChooseTrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_trash)
        val trash : Trash = Trash(ID = "2gwthHYXd7mCa4egwTvr", description = "sztywny opis")
        val LDpicture = DataBase(this).getTrashPhoto(trash)
        ChooseTrashActivityTextViewId.setText(trash.description)

        LDpicture.observe(this, Observer {
            if (it != null) {
                var bitmapdata: ByteArray// let this be your byte array
                bitmapdata = it
                val bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)
                ChooseTrashActivityImageViewId.setImageBitmap(bitmap)
            }
            Log.d("DUPA", "DUPA")
        })


    }

}