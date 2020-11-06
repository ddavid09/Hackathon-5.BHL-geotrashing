package bhl.geotrashing.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import bhl.geotrashing.app.cleantrashactivities.CleanTrashActivity

class MainActivity : AppCompatActivity() {

    lateinit var cleanTrashIntent: Intent
    lateinit var rankingIntent: Intent
    lateinit var reportTrashIntent: Intent
    lateinit var verifyTrashIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cleanTrashIntent = Intent(this, CleanTrashActivity::class.java)
        rankingIntent = Intent(this, RankingActivity::class.java)
        reportTrashIntent = Intent(this, ReportTrashActivity::class .java)
        verifyTrashIntent = Intent(this, VerifyTrashActivity::class.java)
    }

    fun startCleanTrashActivity(view: View)
    {
        startActivity(this.cleanTrashIntent)
    }

    fun startRankingActivity(view: View)
    {
        startActivity(this.rankingIntent)
    }

    fun startReportTrashActivity(view: View)
    {
        startActivity(this.reportTrashIntent)
    }

    fun startVerifyTrashActivity(view: View)
    {
        startActivity(this.verifyTrashIntent)
    }
}