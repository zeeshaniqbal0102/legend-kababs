package com.app.legendkebabs.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.legendkebabs.R
import com.app.legendkebabs.di.PrefManager
import com.app.legendkebabs.di.PrefModule
import com.app.legendkebabs.ui.OrdersActivity
import com.app.legendkebabs.ui.events.TokenRefreshEvent
import com.app.legendkebabs.utils.NotificationUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    private var notificationUtils: NotificationUtils? = null
    private var broadcaster: LocalBroadcastManager? = null

    private val TAG = "FireBaseMessagingService"
    var NOTIFICATION_CHANNEL_ID = "net.larntech.notification"
    val NOTIFICATION_ID = 100

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("message", "Message Received ...");
//        Log.e("message", "Message Received ...data... ${remoteMessage.data}");
//        Log.e("message", "Message Received ...data... ${remoteMessage}")

        if (remoteMessage.data.size > 0) {
            Log.e("message", "Message Received ... ${remoteMessage.data["title"]}");
            Log.e("message", "Message Received ... ${remoteMessage.data["body"]}");
            Log.e("message", "Message Received data... ${remoteMessage.data}");

            val title = remoteMessage.data["title"]
            val body = remoteMessage.data["body"]
            showNotification(applicationContext, title, body)
        } else {
            val title = remoteMessage.notification!!.title
            val body = remoteMessage.notification!!.body
            showNotification(applicationContext, title, body)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val pref = PrefManager(PrefModule.provideSharedPreferences(), Gson())
        pref.putToken(token)

        println("onNewToken $token")

//        EventBus.getDefault().post(TokenRefreshEvent(token))
        val intent = Intent("MyData")
        intent.putExtra("token", token)
        broadcaster!!.sendBroadcast(intent)
    }

    fun showNotification(
        context: Context,
        title: String?,
        message: String?
    ) {
        val ii: Intent
        ii = Intent(context, OrdersActivity::class.java)
//        ii.putExtra("is_notification", true)
//        ii.putExtra("body", message)
        ii.data = Uri.parse("custom://" + System.currentTimeMillis())
        ii.action = "actionstring" + System.currentTimeMillis()
        ii.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pi =
            PendingIntent.getActivity(context, 0, ii, PendingIntent.FLAG_UPDATE_CURRENT)
        val icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)

        val notification: Notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Log.e("Notification", "Created in up to orio OS device");
            notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setSmallIcon(getNotificationIcon())
                .setContentText(message)
                .setAutoCancel(true)
//                .setLargeIcon(icon)
                .setContentIntent(pi)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setWhen(System.currentTimeMillis())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                title,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(NOTIFICATION_ID, notification)
        } else {
            notification = NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
//                .setLargeIcon(icon)
                .setAutoCancel(true)
                .setContentText(message)
                .setContentIntent(pi)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(title).build()
            val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun getNotificationIcon(): Int {
        val useWhiteIcon =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
        return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
    }
}