package bhl.geotrashing.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import bhl.geotrashing.app.firestore.DataBase
import kotlinx.android.synthetic.main.activity_ranking.*

class RankingActivity : AppCompatActivity() {
    data class User(val nickname: String, val points: Int)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val db = DataBase(this)
        val rankingMl = db.getRanking()

        rankingMl.observe(this, Observer {data ->
            if (data!=null)
            {
                val listView = ArrayList(data.values)
                val retList:java.util.ArrayList<User>  = ArrayList()
                for(user in listView){
                    retList.add(User(user.nickname, user.points))
                }
                val listViewSorted = retList.sortedByDescending { it.points }
                val nicknamesList: ArrayList<String> = ArrayList()
                val pointsList: ArrayList<Int> = ArrayList()
                for(i in listViewSorted){
                    nicknamesList.add(i.nickname)
                    pointsList.add(i.points)
                }
                val adapterUserNames: ArrayAdapter<String> = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line, nicknamesList
                )
                RankingActivityListViewUserNames.adapter = adapterUserNames
                val adapterPoints: ArrayAdapter<Int> = ArrayAdapter(
                        this,
                        android.R.layout.simple_dropdown_item_1line, pointsList
                )
                RankingActivityListViewPoints.adapter = adapterPoints

                Log.d("RankingActivity", listView.toString())
                Log.d("RankingActivity", retList.toString())

            }else{
                Log.d("RankingActivity", "ELSE")
            }
        })
    }
}