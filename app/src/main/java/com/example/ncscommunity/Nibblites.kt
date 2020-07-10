package com.example.ncscommunity

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_nibblites.*
import kotlinx.android.synthetic.main.nib_row.*
import kotlinx.android.synthetic.main.nib_row.view.*
import okhttp3.*
import java.io.IOException

class Nibblites : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nibblites)

        recyclerView_nib.layoutManager = LinearLayoutManager(this)
        recyclerView_nib.adapter = Mainadapter()

        fetchJson()


//        nib_git.setOnClickListener{
//            ContextCompat.startActivity(
//                Intent(
//                    Intent.(ACTION_DIAL,
//                    Uri.parse("tel:" + Uri.encode()))
//        }
    }

    private fun fetchJson () {
        println ("Fetching data ..")

        val url = "https://ojuswi.pythonanywhere.com/Nibblites/users/?format=json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response?.body()?.string()
                println(body)

                val gson = GsonBuilder().create()
                val Feed = gson.fromJson(body,Array<feed>::class.java)
               // for (x in 0 until Feed.size)
                    //println(Feed[x].full_name)
            }
            override fun onFailure(call: Call, e: IOException) {
               println("Failed to request data from nibblites api")
            }
        })
    }
}
class feed (val full_name: String , val email: String)


