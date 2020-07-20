package com.example.ncscommunity

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import okhttp3.*

val TAG = "My Firebase"

class FirebaseService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d( TAG, "My Firebase Token: $token")
        sendTokenToServer(token)
    }
}
fun sendTokenToServer(token: String ) {
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
        .addHeader("Authorization", "Token 4cace28496ff6f474e2b2357097b6ac2c46ea961")
        .build()
    val response: Response = client.newCall(request).execute()
    print(response)
}