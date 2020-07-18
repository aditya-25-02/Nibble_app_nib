package com.example.ncscommunity

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.forgotpass_alert.*

class login_page : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        // Initialize Firebase Auth
        auth = Firebase.auth
        loginbtn.setOnClickListener{
            loginUser()
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
        forgot_btn.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Generate reset link")
            val view = layoutInflater.inflate(R.layout.forgotpass_alert,null)

            val user = view.findViewById<EditText>(R.id.forgot_email)

            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
                forgotpass(user)
            })
            builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ ->  })
            builder.show()
        }
    }

    private fun forgotpass(user: EditText) {
        if(user.text.toString().isEmpty()){
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user.text.toString()).matches()){
            return
        }
        Firebase.auth.sendPasswordResetEmail(user.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext,"Reset link sent !",
                          Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(baseContext,"Something went wrong , please try again ! !",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun updateUI(currentUser : FirebaseUser?) {
        if(currentUser != null){
            startActivity(Intent(this,Main2Activity::class.java))
            finish()
        }
        else {
            Toast.makeText(baseContext, "Authentication failed.",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun loginUser (){
        if(user_input.text.toString().isEmpty()){
            user_input.error="Please enter Username/Email"
            user_input.requestFocus()
            return
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(user_input.text.toString()).matches()){
            user_input.error="Please enter valid email"
            user_input.requestFocus()
            return
        }
        if(pass_input.text.toString().isEmpty()){
            pass_input.error="Please enter the password"
            pass_input.requestFocus()
            return
        }
        auth.signInWithEmailAndPassword(user_input.text.toString(), pass_input.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    updateUI(currentUser)

                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
