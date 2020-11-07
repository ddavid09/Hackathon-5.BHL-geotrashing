package bhl.geotrashing.app.cleantrashactivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import bhl.geotrashing.app.R
import bhl.geotrashing.app.firestore.DataBase
import bhl.geotrashing.app.firestore.Trash

class ChooseTrashActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_trash)


    }
}