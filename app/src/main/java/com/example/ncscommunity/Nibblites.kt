package com.example.ncscommunity

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_nibblites.*
import kotlinx.android.synthetic.main.nib_row.*
import kotlinx.android.synthetic.main.nib_row.view.*
import okhttp3.*
import java.io.IOException
import java.net.URL

class Nibblites : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nibblites)

        recyclerView_nib.layoutManager = LinearLayoutManager(this)
        fetchJson()
    }

    private fun fetchJson () {
        println ("Fetching data ..")

        val url = "https://ojuswi.pythonanywhere.com/Nibblites/unsecuremembers/"

        //val request = Request.Builder().url(url).build()
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                val gson = GsonBuilder().create()

                val homefeed = gson.fromJson(body,Array<Homefeed>::class.java )

                //run it on the thread otherwise it will cause an error
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



