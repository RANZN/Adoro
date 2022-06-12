package com.hm.mmmhmm.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.hm.mmmhmm.Chat_Module.InboxActivity
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
            bm = generateBitmapFromVectorDrawable(R.drawable.notification_like)
            showNotification(notificationText, bm!!)

        } else if (type.equals("group")) {
            notificationText = "You have $count members in group"
            bm = generateBitmapFromVectorDrawable(R.drawable.notification_people)
            showNotification(notificationText, bm!!)

        } else if (type.equals("chat")) {
            val person = message.data["person"]
            bm = generateBitmapFromVectorDrawable(R.drawable.notification_chat)
            showChatNotification(count, person, bm)
        }
    }

    private fun showChatNotification(chat: String?, person: String?, image: Bitmap) {
        val intent = Intent(this, InboxActivity::class.java)
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
//            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(person)
            .setContentText(chat)
            .setSound(defaultSoundUri)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        notificationManager.notify((0..10000).random(), notification.build())
    }


    private fun showChatNotification2(chat: String?, person: String?, image: Bitmap) {
        val intent = Intent(this, InboxActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val remoteViews = RemoteViews(this.packageName, R.layout.notification_layout1)
        remoteViews.setTextViewText(R.id.title, person)
        remoteViews.setTextViewText(R.id.text, chat)
        remoteViews.setBitmap(R.id.image, image.toString(), image)


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
            .setContent(remoteViews)
//            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setSound(defaultSoundUri)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        notificationManager.notify((0..10000).random(), notification.build())
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
//            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setSound(defaultSoundUri)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)


        notificationManager.notify((0..10000).random(), notification.build())
    }

    private fun generateBitmapFromVectorDrawable(drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(this, drawableId) as Drawable
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

}