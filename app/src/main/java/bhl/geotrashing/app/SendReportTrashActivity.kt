package bhl.geotrashing.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_send_report_trash.*
import java.io.FileInputStream


class SendReportTrashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send_report_trash)
        val intent : Intent = getIntent()
        val takenImage = intent.getStringExtra("path")
        val decodedTakenImage = BitmapFactory.decodeFile(takenImage)
        activity_send_report_trash_imageView.setImageBitmap(decodedTakenImage)
    }
}
