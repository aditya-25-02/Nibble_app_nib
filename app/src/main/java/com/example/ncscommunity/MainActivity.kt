package com.example.ncscommunity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        skipbtn.setOnClickListener{
            val i = Intent (this,login_page::class.java)
            startActivity(i)
        }
        // Initialize Firebase Auth
        auth = Firebase.auth
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }
    private fun updateUI(currentUser : FirebaseUser?) {
        if(currentUser != null){
            Toast.makeText(baseContext, "Logging-in",
                Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
        else {
            Toast.makeText(baseContext, "Please Login",
                Toast.LENGTH_SHORT).show()
        }
    }
}
