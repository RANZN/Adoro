package com.hm.mmmhmm.helper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.hm.mmmhmm.R
import java.util.*


class FirebaseMessaging : FirebaseMessagingService() {
    var notificationManager: NotificationManager? = null
    var CHANNEL_ID = "111"
    var CHANNEL_NAME = "Adoro Channel"
    var CHANNEL_DESCRIPTION = "New Notification"
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        //Setting up Notification channels for android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels()
        }
        val notificationId = Random().nextInt(60000)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground) //a resource for your custom small icon
            .setContentTitle(remoteMessage.notification?.title) //the "title" value you sent in your notification
            .setContentText(remoteMessage.notification?.body) //ditto
            .setAutoCancel(true) //dismisses the notification on click
            .setSound(defaultSoundUri)
               notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        assert(notificationManager != null)
        notificationManager!!.notify(
            notificationId /* ID of notification */,
            notificationBuilder.build()
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels() {
        val adminChannelName: CharSequence = CHANNEL_NAME
        val adminChannelDescription = CHANNEL_DESCRIPTION
        val adminChannel: NotificationChannel
        adminChannel =
            NotificationChannel(CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW)
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        if (notificationManager != null) {
            notificationManager!!.createNotificationChannel(adminChannel)
        }
    }
}