package com.example.ncscommunity

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_schedule.*
import okhttp3.*
import java.io.IOException

class schedule : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val token = intent.getStringExtra("token")
        print(token)

        backbtn2.setOnClickListener{
            val i = Intent(this,Main2Activity::class.java)
            startActivity(i)
            finish()
        }
        fetchJSON()
    }

    private fun fetchJSON() {
        val url = "https://ojuswi.pythonanywhere.com/Attend/Schedule/"

        val token1 = login_page.Preferences.getAccessToken(this)
        val Token = "Token "+ token1

        val request = Request.Builder()
            .url(url)
            .method("GET",null)
            .addHeader("Authorization",Token)
            .build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)
                val gson = GsonBuilder().create()
                val sch = gson.fromJson(body,Array<Sch>::class.java )

                val current_sch = sch[0]

                val lab_date = current_sch.start_date
                val organizer_name = current_sch.organizer.full_name + ","
                val lab_topic = "Today's lab topic is "+ current_sch.topic
                val lab_timing = "Lab will start at "+ current_sch.start_time + " sharp"
                val lab_name = "Today's lab is at " + current_sch.venue.venue_name
                val spcl_note = current_sch.additional_info

                runOnUiThread {
                    sch_lab_date.text = lab_date
                    sch_announcer.text = organizer_name
                    sch_topic.text = lab_topic
                    sch_lab_time.text = lab_timing
                    sch_lab_venue.text = lab_name
                    if(spcl_note == ""){
                        sch_lab_sp_msg.visibility = View.INVISIBLE
                        sch_lab_sp.visibility = View.INVISIBLE
                    }
                    else {
                        sch_lab_sp_msg.text = spcl_note
                        sch_lab_sp_msg.movementMethod = ScrollingMovementMethod()
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to request data from schedule api")
            }
        })
    }
}
class Sch   (val start_date: String , val start_time : String , val topic : String , val venue : Venue ,val additional_info  : String ,val organizer: Organizer)
class Venue (val venue_name: String)
class Organizer(val full_name: String)