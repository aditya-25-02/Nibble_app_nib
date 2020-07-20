package com.example.ncscommunity

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import okhttp3.*

val TAG = "My Firebase"

class FirebaseService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d( TAG, "My Firebase Token: $token")
        val django_token = login_page.Preferences.getAccessToken(this)
        val Token = "Token " + django_token
        sendTokenToServer(token,Token)
    }
}
fun sendTokenToServer(token: String , Token: String ) {
    val client = OkHttpClient().newBuilder()
        .build()
    val mediaType: MediaType? = MediaType.parse("text/plain")
    val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("registration_id", token)
        .addFormDataPart("type", "android")
        .build()
    val request: Request = Request.Builder()
        .url("https://ojuswi.pythonanywhere.com/Accounts/devices/")
        .method("POST", body)
        .addHeader("Authorization", Token)
        .build()
    val response: Response = client.newCall(request).execute()
    print(response)
}