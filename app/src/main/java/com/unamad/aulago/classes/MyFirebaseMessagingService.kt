package com.unamad.aulago.classes

import android.annotation.SuppressLint
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.unamad.aulago.Utils
import java.util.*

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService: FirebaseMessagingService(){
/*

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun generateNotification(title: String, message: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        else PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = Notification.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.book)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)

        //builder = builder.setCustomContentView(getRemoteView(title, message))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = NotificationChannel(CHANNEL_ID,
            CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.notify(0, builder.build())

    }*/

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.notification != null){

            Utils.showNotification(title = message.notification!!.title!!, description = message.notification!!.body!!, context = this)
        }
    }

    override fun onNewToken(token: String) {
        //Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token)
    }

    companion object {
        private const val CHANNEL_ID = "NOTIFICATION_CHANNEL"
        private const val CHANNEL_NAME = "com.unamad.profego"
    }

}