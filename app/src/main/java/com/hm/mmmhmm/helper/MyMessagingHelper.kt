package com.hm.mmmhmm.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hm.mmmhmm.R
import com.hm.mmmhmm.activity.MainActivity


class MyMessagingHelper : FirebaseMessagingService() {
    private val channelId = "com.adoro.notification"
    private val channelName = "com.adoro.notification"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val type = message.data["type"]
        val count = message.data["count"]
        var notificationText = ""
        var bm: Bitmap? = null
        if (type.equals("like")) {
            notificationText = "You have got $count likes on your post"
            bm = BitmapFactory.decodeResource(resources, R.drawable.notification_like)
        } else if (type.equals("group")) {
            notificationText = "You have $count members in group"
            bm = BitmapFactory.decodeResource(resources, R.drawable.notification_people)
        }
//        val imageUrl = message.data["image"]
//        val url = URL(imageUrl)
//        val image = BitmapFactory.decodeStream(url.openConnection().getInputStream())
//        Log.e("ranzn", "onMessageReceived: $type $likeCount $image")
        showNotification(notificationText,  bm!!)
    }


    private fun showNotification(title: String?, image: Bitmap) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelName, channelId, importance)
            channel.description = "${application.packageName} Notifications"
            channel.setShowBadge(true)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            notificationManager.createNotificationChannel(channel)
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.app_icon_transparent)
            .setLargeIcon(image)
            .setContentTitle(title)
            .setSound(defaultSoundUri)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        notificationManager.notify((0..10000).random(), notification.build())
    }

}