package bhl.geotrashing.app.cleantrashactivities

import android.content.Intent
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

    lateinit var confirmTrashIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_trash)
        confirmTrashIntent = Intent(this, ConfirmTrashActivity::class.java)

        val trash : Trash = Trash(ID = "2gwthHYXd7mCa4egwTvr", description = "sztywny opis")
        val trashId = trash.ID
        val LDpicture = DataBase(this).getTrashPhoto(trash)
        ChooseTrashActivityTextViewId.setText(trash.description)

        LDpicture.observe(this, Observer {
            if (it != null) {
                //val bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)
                ChooseTrashActivityImageViewId.setImageBitmap(it)
            } else{
                Log.d("error", "error")
            }

        })
        ChooseTrashActivityButtonId.setOnClickListener {
            confirmTrashIntent.putExtra("trashId", trashId)
            startActivity(this.confirmTrashIntent)
        }


    }


}