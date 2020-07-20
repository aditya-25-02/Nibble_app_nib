package com.example.ncscommunity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
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


class login_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        loginbtn.setOnClickListener{
            loginUser()
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
                    if(response.code()==200){
                        val Token = gson.fromJson(responsebody,Auth_token::class.java)

                        FirebaseInstanceId.getInstance().instanceId
                            .addOnCompleteListener(OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "getInstanceId failed", task.exception)
                                    return@OnCompleteListener
                                }
                                // Get new Instance ID token
                                val token = task.result?.token

                                val client = OkHttpClient().newBuilder()
                                    .build()
                                val mediaType: MediaType? = MediaType.parse("text/plain")
                                val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
                                    .addFormDataPart("registration_id",token)
                                    .addFormDataPart("type", "android")
                                    .build()
                                val request: Request = Request.Builder()
                                    .url("https://ojuswi.pythonanywhere.com/Accounts/devices/")
                                    .method("POST", body)
                                    .addHeader("Authorization", "Token "+Token.auth_token)
                                    .build()
                                GlobalScope.launch (Dispatchers.Main) {
                                    val response = withContext(Dispatchers.IO) { client.newCall(request).execute()}
                                    println("Firebase token sent ! (if not already - 201..ie saved now) (if already saved 400) ")
                                }
                            })


                        Preferences.setAccessToken(this@login_page,Token.auth_token)
                        val i = Intent(this@login_page,Main2Activity::class.java)
                        i.putExtra("token",Token.auth_token)
                        startActivity(i)
                        finish()
                    }
                }
                override fun onFailure(call: Call, e: IOException) {
                    print("Failed to log in")
                }

            })
            finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })
    thread.start()
    }
}
class Auth_token (val auth_token : String)
