package com.example.ncscommunity

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
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

        backbtn2.setOnClickListener{
            val i = Intent(this,Main2Activity::class.java)
            startActivity(i)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        fetchJSON()
    }

    private fun fetchJSON() {


        //Loading..
        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

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

                // if no lab found then show alert box to refresh or leave the activity
                if (body == "[]") {

                    // first disabling the loading animation
                    dialog.dismiss()

                    runOnUiThread(){
                        //building alert box
                        val builder = AlertDialog.Builder(this@schedule)
                        builder.setTitle("No lab found right now! ")
                        builder.setMessage("Wanna refresh page or leave?")
                        builder.setPositiveButton("refresh",{ dialogInterface: DialogInterface, i: Int ->
                            fetchJSON()
                        })
                        builder.setNegativeButton("leave",{ dialogInterface: DialogInterface, i: Int ->
                            finish()
                        })
                        builder.show()
                    }

                }
                // if lab found then show the data in the activity

                else {
                    val gson = GsonBuilder().create()
                    val sch = gson.fromJson(body, Array<Sch>::class.java)

                    val current_sch = sch[0]

                    val lab_date = current_sch.start_date
                    val organizer_name = current_sch.organizer.full_name + ","
                    val lab_topic = "Lab topic is " + current_sch.topic
                    val lab_timing = "Lab will start at " + current_sch.start_time + " sharp"
                    val lab_name = "Lab is at " + current_sch.venue.venue_name
                    val spcl_note = current_sch.additional_info

                    runOnUiThread {
                        sch_lab_date.text = lab_date
                        sch_announcer.text = organizer_name
                        sch_topic.text = lab_topic
                        sch_lab_time.text = lab_timing
                        sch_lab_venue.text = lab_name
                        if (spcl_note == "") {
                            sch_lab_sp_msg.visibility = View.INVISIBLE
                            sch_lab_sp.visibility = View.INVISIBLE
                        } else {
                            sch_lab_sp_msg.text = spcl_note
                            sch_lab_sp_msg.movementMethod = ScrollingMovementMethod()
                        }
                        dialog.dismiss()
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