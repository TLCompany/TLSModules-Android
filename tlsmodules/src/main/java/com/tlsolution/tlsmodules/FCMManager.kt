package com.tlsolution.tlsmodules

import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tlsolution.tlsmodules.R

@RequiresApi(Build.VERSION_CODES.O)
class FCMManager: FirebaseMessagingService() {


    companion object {
        val TAG = "FirebaseMessaging"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        check(remoteMessage != null) {
            return
        }

        sendNotification(remoteMessage)
    }

    fun sendNotification(remoteMessage: RemoteMessage) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, "General")
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(defaultSoundUri)
            .setColor(ContextCompat.getColor(this, R.color.notofication_color))
//            .setSmallIcon(R.drawable.notification_icon)
            .setStyle(NotificationCompat.BigTextStyle())

        with(NotificationManagerCompat
            .from(this)) {
            notify(1, notificationBuilder.build())
        }
    }
}
