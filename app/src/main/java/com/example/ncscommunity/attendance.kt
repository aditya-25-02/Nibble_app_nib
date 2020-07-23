package com.example.ncscommunity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.activity_schedule.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.text.SimpleDateFormat
import java.util.*

class attendance : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        // showing Today's date on screen
        val today  = Calendar.getInstance()
        val date = SimpleDateFormat("MMMM d,Y").format(today.time)
        attendance_date.text = date


        scan_btn.setOnClickListener {

            // Scanner code
            val scanner =  IntentIntegrator(this)
            // only scanning QR CODE
            scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            // message on the screen
            scanner.setPrompt("Scan NCS QR code")
            scanner.initiateScan()
        }
        backbtn3.setOnClickListener {
            val i = Intent(this,Main2Activity::class.java)
            startActivity(i)
            finish()
        }
    }
    // result of scanner screen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        // if scanned if a QR code
        if(resultCode== Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

            if(result != null) {
                // if the content in the result is null
                if(result.contents == null){
                    Toast.makeText(this,"Cancelled",Toast.LENGTH_SHORT).show()
                }
                // if it contains something
                else {
                    Toast.makeText(
                        this,
                        "Scanned !!",
                        Toast.LENGTH_SHORT
                    ).show()

                    //getting django token from local machine
                    val token1 = login_page.Preferences.getAccessToken(this)
                    val Token = "Token " + token1

                    // sending scan request to the lab url with token ( so the backend can check which user is scanning the code)
                    val client = OkHttpClient().newBuilder()
                            .build()
                        val mediaType = MediaType.parse("text/plain")
                        val body = RequestBody.create(mediaType, "")
                        val request = Request.Builder()
                            .url("https://ojuswi.pythonanywhere.com/Attend/" + result.contents + "/")
                            .method("POST", body)
                            .addHeader("Authorization",Token)
                            .build()
                    GlobalScope.launch (Dispatchers.Main) {
                         val response = withContext(Dispatchers.IO){ client.newCall(request).execute()}
                        println(response)

                        // QR Scanned (creating the dialog
                        var dialog = Dialog(
                            this@attendance,
                            android.R.style.Theme_Translucent_NoTitleBar
                        )

                        //creating view var
                        var view : View? = null

                        // if attendance is

                        // marked
                        if(response.code()==201) {
                             view = this@attendance.layoutInflater.inflate(
                                R.layout.custom_attendance_marked,
                                null
                            )
                        }
                        // already marked
                        if(response.code()==208) {
                            view = this@attendance.layoutInflater.inflate(
                                R.layout.custom_already_marked,
                                null
                            )
                        }
                        // scanning the wrong QR
                        if(response.code()==404){
                            view = this@attendance.layoutInflater.inflate(
                                R.layout.custom_wrong_lab,
                                null
                            )
                        }
                        // Scaning right QR but not the right lab
                        if(response.code()==410){
                            view = this@attendance.layoutInflater.inflate(
                                R.layout.custom_no_lab,
                                null
                            )
                        }

                        // showing the respective animation
                        if (view != null) {
                            dialog.setContentView(view)
                        }
                        dialog.setCancelable(true)
                        dialog.show()
                    }
                }
            }
            else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }
}