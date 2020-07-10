package com.example.ncscommunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_schedule.*
import java.text.SimpleDateFormat
import java.util.*

class schedule : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val today  = Calendar.getInstance()
        val date = SimpleDateFormat("MMMM d,Y").format(today.time)
        text_view_date.text = date

        backbtn2.setOnClickListener{
            val i = Intent(this,Main2Activity::class.java)
            startActivity(i)
        }
    }

}
