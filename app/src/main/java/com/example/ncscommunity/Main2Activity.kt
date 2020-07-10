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
        backbtn.setOnClickListener{
            val i = Intent(this,MainActivity::class.java)
            startActivity(i)
        }
        schedulebtn.setOnClickListener {
            val j = Intent(this, schedule::class.java)
            startActivity(j)
        }
        jobsbtn.setOnClickListener{
            val snack =Snackbar.make(it,"Feature to be added soon !!",Snackbar.LENGTH_LONG)
            snack.show()
        }
        nibblitesbtn.setOnClickListener {
            val i = Intent(this,Nibblites::class.java)
            startActivity(i)
        }
    }
}
