package com.example.ncscommunity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_nibblites.*
import kotlinx.android.synthetic.main.activity_projects.*
import okhttp3.*
import java.io.IOException

class projects : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)
        fetchJson()
        recyclerView_projects.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL, false)
    }
    private fun fetchJson () {
        println ("Fetching project data ..")

        val url = "https://ojuswi.pythonanywhere.com/Projects/listall/?format=json"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                val gson = GsonBuilder().create()

                val projectfeed = gson.fromJson(body,Array<Projectfeed>::class.java)

                runOnUiThread {
                    recyclerView_projects.adapter = projectAdapter(projectfeed,this@projects)
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to request data from projects api")
            }
        })
    }
}
class Projectfeed (val team : Array<Team>  , val project_name: String , val project_description: String ,val techstack: Techstack ,val background: String ,val github: String , val started_year: Int ,val last_modified: Int , val icon: String)
class Team (val full_name : String)
class Techstack( val languages :Array<Languages> , val frontend_techs : Array<Frontend>, val backend_techs : Array<Backend> , val app_techs : Array<App>)
class Languages(val language : String)
class Frontend (val frontend_tech: String)
class Backend (val backend_tech: String)
class App (val app_tech: String)