package bhl.geotrashing.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var trash: Trash

    override fun onCreate(savedInstanceState: Bundle?) {
        verifyTrashIntent = Intent(this, VerifyTrashActivity::class.java)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_trash)

        val trashMl = DataBase(this).getTrashToConfirm()

        trashMl.observe(this, Observer { it ->
            if(it != null){
                val picture = DataBase(this).getTrashPhoto(it, false)
                val picture2 = DataBase(this).getTrashPhoto(it, true)
                trash = it
                picture.observe(this, Observer {it2 ->
                    if (it2 != null) {
                        activity_first_verify_trash_imageViewId.setImageBitmap(it2)
                        firstReady = true
                        checkReady()
                    } else{
                        Log.d("error", "error")
                    }
                })

                picture2.observe(this, Observer {it2 ->
                    if (it2 != null) {
                        activity_second_verify_trash_imageViewId.setImageBitmap(it2)
                        secondReady = true
                        checkReady()
                    } else{
                        Log.d("error", "error")
                    }
                })
            }
            else{
                Toast.makeText(this, "Potwierdzono wszystkie przypadki", Toast.LENGTH_LONG).show()
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