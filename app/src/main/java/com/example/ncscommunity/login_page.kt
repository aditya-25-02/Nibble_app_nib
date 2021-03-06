package com.example.ncscommunity

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_login_page.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit


class login_page : AppCompatActivity() {

    lateinit var remember : CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        loginbtn.setOnClickListener{
            loginUser()
        }
//        forgot_btn.setOnClickListener{
//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Generate reset link")
//            val view = layoutInflater.inflate(R.layout.forgotpass_alert,null)
//
//            val user = view.findViewById<EditText>(R.id.forgot_email)
//
//            builder.setView(view)
//            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { _, _ ->
//                forgotpass(user)
//            })
//            builder.setNegativeButton("close", DialogInterface.OnClickListener { _, _ ->  })
//            builder.show()
//        }
    }
    public object Preferences {
        fun setAccessToken(context: Context, token: String?) {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("ACCESSTOKEN", token)
            editor.apply()
        }
        fun getAccessToken(context: Context): String? {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
            return sharedPreferences.getString("ACCESSTOKEN", null)
        }
    }

//    private fun forgotpass(user: EditText) {
//        if(user.text.toString().isEmpty()){
//            return
//        }
//        if(!Patterns.EMAIL_ADDRESS.matcher(user.text.toString()).matches()){
//            return
//        }
//        Firebase.auth.sendPasswordResetEmail(user.text.toString())
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    Toast.makeText(baseContext,"Reset link sent !",
//                          Toast.LENGTH_SHORT).show()
//                }
//                else {
//                    Toast.makeText(baseContext,"Something went wrong , please try again ! !",
//                        Toast.LENGTH_SHORT).show()
//                }
//            }
//    }
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

        //Loading..
        var dialog = Dialog(this,android.R.style.Theme_Translucent_NoTitleBar)
        val view = this.layoutInflater.inflate(R.layout.custom_loading_effect,null)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.show()
    val thread = Thread(Runnable {
        try {
            val client = OkHttpClient()
            val mediaType: MediaType? = MediaType.parse("text/plain")
            val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("email", user_input.text.toString())
                .addFormDataPart("password", pass_input.text.toString())
                .build()
            val request: Request = Request.Builder()
                .url("https://ojuswi.pythonanywhere.com/Accounts/token/login/")
                .method("POST", body)
                .build()
            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responsebody = response.body()?.string()
                    println(responsebody)
                    val gson = GsonBuilder().create()

                    // If the user and password is correct

                    if (response.code() == 200) {

                        //Acces the token sent by django bakend in the response body

                        val Token = gson.fromJson(responsebody, Auth_token::class.java)

                        //Getting instance ID of firebase

                        FirebaseInstanceId.getInstance().instanceId
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "getInstanceId failed", task.exception)
                                    return@OnCompleteListener
                                }
                                // Get new Instance ID token
                                val token = task.result?.token

                                // After getting instance ID sending it to ojuswi Backend to enable firebase Push notification
                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val mediaType: MediaType? = MediaType.parse("text/plain")
                                val body: RequestBody =
                                    MultipartBody.Builder().setType(MultipartBody.FORM)
                                        .addFormDataPart("registration_id", token)
                                        .addFormDataPart("type", "android")
                                        .build()
                                val request: Request = Request.Builder()
                                    .url("https://ojuswi.pythonanywhere.com/Accounts/devices/")
                                    .method("POST", body)
                                    .addHeader("Authorization", "Token " + Token.auth_token)
                                    .build()
                                GlobalScope.launch(Dispatchers.Main) {
                                    val response = withContext(Dispatchers.IO) {
                                        client.newCall(request).execute()
                                    }
                                    println("Firebase token sent ! (if not already - 201..ie saved now) (if already saved 400) ")
                                }
                            })

                        // Saving the token to local machine..

                        remember = findViewById(R.id.remember_me) as CheckBox

                        if(remember.isChecked) {
                            Preferences.setAccessToken(this@login_page, Token.auth_token)
                        }
                        dialog.dismiss()
                        val i = Intent(this@login_page, Main2Activity::class.java)
                        startActivity(i)
                        finish()
                    }
                    else {
                        runOnUiThread() {
                            dialog.dismiss()
                            Toast.makeText(
                                this@login_page,
                                "Wrong credentials!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    dialog.dismiss()
                    print("Failed to log in....(internet error / backend error")
                }

            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })
    thread.start()
    }
}
class Auth_token (val auth_token : String)
