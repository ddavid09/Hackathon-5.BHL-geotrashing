package bhl.geotrashing.app.cleantrashactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bhl.geotrashing.app.MainActivity
import bhl.geotrashing.app.R
import kotlinx.android.synthetic.main.activity_prize_trash.*
import kotlinx.android.synthetic.main.activity_prize_trash.topAppBar
import kotlinx.android.synthetic.main.activity_send_report_trash.*

class PrizeTrashActivity : AppCompatActivity() {
    lateinit var prizeTrashIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prize_trash)
        prizeTrashIntent = Intent(this, MainActivity::class.java)

        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.send -> {
                    startActivity(this.prizeTrashIntent)
                    true
                }
                else -> false
            }

        }

    }
}