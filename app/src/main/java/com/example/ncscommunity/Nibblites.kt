package com.example.ncscommunity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_nibblites.*
import okhttp3.*
import java.io.IOException


class Nibblites : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nibblites)
        recyclerView_nib.layoutManager = LinearLayoutManager(this)
    }
    override fun onStart() {
        super.onStart()
        fetchJson()
    }

    private fun fetchJson () {

        println ("Fetching nibblites data ..")

        // Loading animation ...
        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

        // django Token from local machine
        val validate =login_page.Preferences.getAccessToken(this)
        val token = "Token "+ validate

        // requesting to the nibblites API

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://ojuswi.pythonanywhere.com/Nibblites/members/")
            .method("GET", null)
            .addHeader("Authorization",token)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                val gson = GsonBuilder().create()
                val homefeed = gson.fromJson(body,Array<Homefeed>::class.java )

                //run it on the UI thread otherwise it will cause an error
                runOnUiThread {
                    recyclerView_nib.adapter = Mainadapter(homefeed , this@Nibblites)
                    dialog.dismiss()
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                dialog.dismiss()
               println("Failed to request data from nibblites api")
            }
        })
    }
}
class Homefeed (val year: String ,val full_name: String ,val email: String , val profile_pic: String , val club: String ,val phone_no: String, val designation: String, val profiles: Array<Links>)
class Links ( val link: String)



