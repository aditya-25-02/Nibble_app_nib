package com.example.ncscommunity

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_projects.*
import okhttp3.*
import java.io.IOException


class projects : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_projects)
        recyclerView_projects.layoutManager = LinearLayoutManager(this,RecyclerView.HORIZONTAL, false)
    }

    override fun onStart() {
        super.onStart()
        //Loading..
        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

        fetchJson(dialog)
    }

    private fun fetchJson (dialog:Dialog) {
        println ("Fetching project data ..")

        //token
        val validate = login_page.Preferences.getAccessToken(this)
        val token = "Token "+ validate

        val client = OkHttpClient().newBuilder()
            .build()
        val request: Request = Request.Builder()
            .url("https://ojuswi.pythonanywhere.com/Projects/list/")
            .method("GET", null)
            .addHeader("Authorization", token)
            .build()

        client.newCall(request).enqueue(object : Callback {

            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)
                val gson = GsonBuilder().create()
                val projectfeed = gson.fromJson(body,Array<Projectfeed>::class.java)
                runOnUiThread {
                    recyclerView_projects.adapter = projectAdapter(projectfeed,this@projects)
                    dialog.dismiss()
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