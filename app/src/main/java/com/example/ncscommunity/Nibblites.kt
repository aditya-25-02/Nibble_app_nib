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

        val url = "https://ojuswi.pythonanywhere.com/Nibblites/users/"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                val gson = GsonBuilder().create()
                val feed = gson.fromJson(body,Array<NibblitesResponse.Nibblite>::class.java)
                for (x in 0 until feed.size)
                    println(feed[x].full_name)
            }
            override fun onFailure(call: Call, e: IOException) {
               println("Failed to request data from nibblites api")
            }
        })
    }
}

class NibblitesResponse {

    data class Nibblite (
        val nickname: String,
        val full_name: String,
        val profile_pic: URL,
        val email: String,
        val year: Int,
        val designation: String,
        val club: String,
        val phone_no: String,
        val user_links: UserLinks
    )

    data class UserLinks (
        val email: String,
        val portfolio: URL,
        val linkedin: URL,
        val github: URL,
        val kaggle: URL,
        val codechef: URL,
        val codeforces: URL,
        val hackerrank: URL,
        val hackerearth: URL,
        val topcoder: URL,
        val codewars: URL,
        val leetcode: URL,
        val spoj: URL,
        val codeingame: URL,
        val behance: URL,
        val medium: URL,
        val fossbyte: URL
    )
}


