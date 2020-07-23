package com.example.ncscommunity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        login_btn.setOnClickListener{
            val i = Intent (this,login_page::class.java)
            startActivity(i)
        }
    }
    // to set auto-login functionality
    public override fun onStart() {
        super.onStart()
        // Showing loading dialogue
        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

        //Getting django token saved in local machine
        val validate =login_page.Preferences.getAccessToken(this)
        print("Current token is " + validate)

        // stting is to standard form
        val validate_token = "Token "+ validate

        // i have set default django token as null
        // so if it is not null , we have info of a user
        if(validate!=null){

            //Creating client and increasing connecttimeout and readtimeout to 30sec to wait for response for a longer time
            val client = OkHttpClient().newBuilder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build()
            // sending django token from local machine to check for validation
            val request: Request = Request.Builder()
                .url("https://ojuswi.pythonanywhere.com/Accounts/users/me/")
                .method("GET", null)
                .addHeader("Authorization", validate_token)
                .build()
            GlobalScope.launch (Dispatchers.Main) {
                // response from backend
                val response = withContext(Dispatchers.IO){ client.newCall(request).execute()}
                //parsing the response code and loading dialogue
                dologin(response.code(),dialog)
            }
        }
        // if it is null that means i have no user information in my local machine...heading the user to login page
        else {
            //dismissing loading animation
            dialog.dismiss()
            Toast.makeText(this,"Please login",Toast.LENGTH_SHORT).show()
        }
    }

    private fun dologin(code: Int , dialog: Dialog) {

        // dismissing loading animation
        dialog.dismiss()

        //if response code is 200 ( Token i had in my local machine is an authorized user in our serve)
        if(code==200){
            // logging in the user to the categories section
            Toast.makeText(this@MainActivity,"Logging you in",Toast.LENGTH_SHORT).show()
            val i = Intent(this@MainActivity,Main2Activity::class.java)
            startActivity(i)
            finish()
        }
        // if response code is not 200 (i.e the user token i have sent is not a valid user .... so expiring it's session
        else{
            Toast.makeText(this@MainActivity,"Your are Unauthorized",Toast.LENGTH_LONG).show()
        }
    }

}
