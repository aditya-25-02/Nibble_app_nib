package com.example.ncscommunity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.*


val TAG = "My Firebase"

class FirebaseService: FirebaseMessagingService() {

    // if a new token is created by firebase this function is called
    override fun onNewToken(token: String) {
        Log.d( TAG, "My Firebase Token: $token")

        //extracting django token
        val django_token = login_page.Preferences.getAccessToken(this)
        val Token = "Token " + django_token

        // On new firebase code i need to send it again to django backend to use push notification service smoothly
        sendTokenToServer(token,Token)
    }

    // To create foreground notification (google handles only background notification)
    //so creating a function to show the same notification if the app is currently using

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("msg", "onMessageReceived: " + remoteMessage.data["message"])
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"

        //androidx.core.app.NotificationCompat
        //androidx.core.app.NotificationCompat.Builder

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body).setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle())
        val manager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder.build())
    }
}

// function to send the newly generated token to django backend

fun sendTokenToServer(token: String , Token: String ) {

    // token -> newly generated firebase token
    // Token -> django token
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