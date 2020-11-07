package bhl.geotrashing.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import bhl.geotrashing.app.firestore.DataBase
import bhl.geotrashing.app.firestore.Trash
import kotlinx.android.synthetic.main.activity_verify_trash.*

class VerifyTrashActivity : AppCompatActivity() {
    private lateinit var verifyTrashIntent: Intent
    private var firstReady = false
    private var secondReady = false
    private var ready = false

    override fun onCreate(savedInstanceState: Bundle?) {
        verifyTrashIntent = Intent(this, VerifyTrashActivity::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_trash)

        val trash = Trash(ID = "6FZrURjFfPLbq7JxHpWv", description = "sztywny opis")
        val picture = DataBase(this).getTrashPhoto(trash, false)
        val picture2 = DataBase(this).getTrashPhoto(trash, true)

        picture.observe(this, Observer {
            if (it != null) {
                activity_first_verify_trash_imageViewId.setImageBitmap(it)
                firstReady = true
                checkReady()
            } else{
                Log.d("error", "error")
            }
        })

        picture2.observe(this, Observer {
            if (it != null) {
                activity_second_verify_trash_imageViewId.setImageBitmap(it)
                secondReady = true
                checkReady()
            } else{
                Log.d("error", "error")
            }
        })

        activity_deny_verify_trash_btnSendId.setOnClickListener {
            if(ready){
                Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show()
                DataBase(this).unconfirmTrashCollecting(trash)
                startActivity(this.verifyTrashIntent)
            }
            else{
                Toast.makeText(this, "Poczekaj na pobranie obydwu zdjęć", Toast.LENGTH_SHORT).show()
            }
        }
        activity_confirm_verify_trash_btnSendId.setOnClickListener {
            if(ready){
                Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show()
                DataBase(this).confirmTrashCollecting(trash)
                startActivity(this.verifyTrashIntent)
            }
            else{
                Toast.makeText(this, "Poczekaj na pobranie obydwu zdjęć", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkReady(){
        ready = firstReady && secondReady
    }
}