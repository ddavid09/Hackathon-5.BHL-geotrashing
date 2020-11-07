package bhl.geotrashing.app.cleantrashactivities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import bhl.geotrashing.app.ReportTrashActivity
import bhl.geotrashing.app.firestore.DataBase
import bhl.geotrashing.app.firestore.Trash
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_choose_trash.*
import kotlinx.android.synthetic.main.activity_confirm_trash.*
import kotlinx.android.synthetic.main.activity_report_trash.*
import kotlinx.android.synthetic.main.activity_send_report_trash.*
import java.io.File

private const val FILE_NAME = "photo2.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class ConfirmTrashActivity : AppCompatActivity() {

    lateinit var prizeTrashActivity: Intent
    lateinit var location: LatLng
    lateinit var path : String
    var btnClickable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_trash)
        prizeTrashActivity = Intent(this, PrizeTrashActivity::class.java)
        val db = DataBase(this)

        val intent : Intent = getIntent()
        val trashId = intent.getStringExtra("trashId")
        val trash : Trash = Trash(ID = trashId)
        val description = trash.description
        location = LatLng(trash.locationGeoPoint.latitude,trash.locationGeoPoint.longitude)


        ConfirmTrashActivityButtonTakePictureId.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile =  getPhotoFile(FILE_NAME)

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile)
            val fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            if (takePictureIntent.resolveActivity(this.packageManager)!=null){
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            }else{
                Toast.makeText(this,"Nie można otworzyć aparatu", Toast.LENGTH_SHORT).show()
            }
        }


        ConfirmTrashActivityButtonConfrimId.setOnClickListener {
            if (btnClickable){

                val decodedTakenImage = BitmapFactory.decodeFile(path)
                db.uploadTrash(location,description.toString() ,decodedTakenImage)
                prizeTrashActivity.putExtra("trashId", trashId)
                startActivity(this.prizeTrashActivity)
            }else
            {
                Toast.makeText(this, "Najpierw zrób zdjęcie!", Toast.LENGTH_SHORT).show()
            }

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            //val takenImage = data?.extras?.get("data") as Bitmap
            path = photoFile.absolutePath
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            ConfirmTrashActivityImageViewId.setImageBitmap(takenImage)
            btnClickable = true
        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getPhotoFile(fileName: String): File{
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }



}