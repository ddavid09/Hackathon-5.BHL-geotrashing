package bhl.geotrashing.app

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_report_trash.*
import java.io.File

private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class ReportTrashActivity : AppCompatActivity() {

    lateinit var sendReportTrashIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_trash)
        sendReportTrashIntent = Intent(this, SendReportTrashActivity::class.java)

        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            //API > 24
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileprovider = FileProvider.getUriForFile(
                this,
                "com.example.fileprovider",
                photoFile
            )
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileprovider)

            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Nie mozna otworzyc aparatu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPhotoFile(fileName: String): File {
        val picturesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(fileName, ".jpg", picturesDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //       val takenImage = data?.extras?.get("data") as Bitmap
            val path = photoFile.absolutePath
            //val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)

            sendReportTrashIntent.putExtra("path", path)
            startActivity(this.sendReportTrashIntent)
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }

    }





}