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
    val client = OkHttpClient()
        .newBuilder()
        .build()
    val mediaType: MediaType? = MediaType.parse("text/plain")
    val body: RequestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("firebase_token", token)
        .build()
    val request: Request = Request.Builder()
        .url("https://ojuswi.pythonanywhere.com/Accounts/users/me/")
        .method("PATCH", body)
        .addHeader("Authorization", "Token 484abdce98871be6ed3dc97d31d4b9da36aac4e0")
        .build()
    val response: Response = client.newCall(request).execute()
}