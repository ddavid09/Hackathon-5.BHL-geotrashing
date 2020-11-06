package bhl.geotrashing.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import bhl.geotrashing.app.firestore.DataBase
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_send_report_trash.*


class SendReportTrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_report_trash)
        val intent : Intent = getIntent()
        val takenImage = intent.getStringExtra("path")
        val decodedTakenImage = BitmapFactory.decodeFile(takenImage)
        val db = DataBase(this)
        activity_send_report_trash_imageViewId.setImageBitmap(decodedTakenImage)
        val description = activity_send_report_trash_descriptionId.text

        activity_send_report_trash_btnSendId.setOnClickListener {
            db.uploadTrash(LatLng(0.0,0.0),description.toString() ,decodedTakenImage)
        }
    }

}
