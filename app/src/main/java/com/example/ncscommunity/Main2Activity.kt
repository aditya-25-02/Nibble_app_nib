package com.example.ncscommunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main2.*
import java.text.SimpleDateFormat
import java.util.*

class Main2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        //user token
        val token = intent.getStringExtra("token")
        println(token)

        schedulebtn.setOnClickListener {
            val j = Intent(this, schedule::class.java)
            j.putExtra("token",token)
            startActivity(j)
        }
        nibblitesbtn.setOnClickListener {
            val i = Intent(this,Nibblites::class.java)
            i.putExtra("token",token)
            startActivity(i)
        }
        projectsbtn.setOnClickListener {
            val i = Intent (this,projects::class.java)
            i.putExtra("token",token)
            startActivity(i)
        }
        attendancebtn.setOnClickListener {
            val i = Intent(this,attendance::class.java)
            i.putExtra("token",token)
            startActivity(i)
        }
        jobsbtn.setOnClickListener{
            val snack =Snackbar.make(it,"Feature to be added soon !!",Snackbar.LENGTH_LONG)
            snack.show()
        }
        performancebtn.setOnClickListener {
            val snack =Snackbar.make(it,"Feature to be added soon !!",Snackbar.LENGTH_LONG)
            snack.show()
        }
    }
}
