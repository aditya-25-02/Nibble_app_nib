package com.example.ncscommunity

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
        fetchJson()
        recyclerView_nib.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchJson () {

        println ("Fetching nibblites data ..")

        //Token
        val validate =login_page.Preferences.getAccessToken(this)
        val token = "Token "+ validate

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
                }
            }
            override fun onFailure(call: Call, e: IOException) {
               println("Failed to request data from nibblites api")
            }
        })
    }
}
class Homefeed (val year: String ,val full_name: String ,val email: String , val profile_pic: String , val club: String ,val phone_no: String, val designation: String, val profiles: Array<Links>)
class Links ( val link: String ,val website : String)



