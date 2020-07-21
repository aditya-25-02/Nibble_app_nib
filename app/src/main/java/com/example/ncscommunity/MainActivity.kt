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
    public override fun onStart() {
        super.onStart()

        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()

        val validate =login_page.Preferences.getAccessToken(this)
        print("Current token is " + validate)

        val validate_token = "Token "+ validate
        if(validate!=null){
            val client = OkHttpClient().newBuilder()
                .connectTimeout(30,TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .build()

            val request: Request = Request.Builder()
                .url("https://ojuswi.pythonanywhere.com/Accounts/users/me/")
                .method("GET", null)
                .addHeader("Authorization", validate_token)
                .build()
            GlobalScope.launch (Dispatchers.Main) {
                val response = withContext(Dispatchers.IO){ client.newCall(request).execute()}
                dologin(response.code(),dialog)
            }
        }
        else {
            dialog.dismiss()
            Toast.makeText(this,"Please Login",Toast.LENGTH_SHORT).show()
        }
    }

    private fun dologin(code: Int , dialog: Dialog) {
        dialog.dismiss()
        if(code==200){
            Toast.makeText(this@MainActivity,"Logging you in",Toast.LENGTH_SHORT).show()
            val i = Intent(this@MainActivity,Main2Activity::class.java)
            startActivity(i)
            finish()
        }
        else{
            Toast.makeText(this@MainActivity,"Your are Unauthorized",Toast.LENGTH_LONG).show()
        }
    }

}
