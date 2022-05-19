package com.tikonsil.tikonsil509.utils.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.channel.ChannelIdNotification.CHANNEL_ID
import com.tikonsil.tikonsil509.utils.channel.ChannelIdNotification.CHANNEL_NAME
import kotlin.random.Random

/** * Created by ISMOY BELIZAIRE on 13/05/2022. */
class FirebaseService: FirebaseMessagingService() {
 override fun onNewToken(token: String) {
  super.onNewToken(token)
 }
 override fun onMessageReceived(message: RemoteMessage) {
  super.onMessageReceived(message)
  val intent = Intent(this, HomeActivity::class.java)
  val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager
  val notificationID = Random.nextInt()
  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
   createNotificationChannel(notificationManager)
  }
  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
  val pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_IMMUTABLE)
  val notification =NotificationCompat.Builder(this,CHANNEL_ID)
   .setContentTitle(message.data["title"])
   .setContentText(message.data["message"])
   .setSmallIcon(R.drawable.logo)
   .setAutoCancel(true)
   .setColor(ContextCompat.getColor(applicationContext, R.color.colorboton))
   .setStyle(NotificationCompat.BigTextStyle().bigText(message.data["message"]).setBigContentTitle(message.data["title"]))
   .setContentIntent(pendingIntent)
   .build()
  notificationManager.notify(notificationID,notification)
 }
 @RequiresApi(Build.VERSION_CODES.O)
 private fun createNotificationChannel(notificationManager: NotificationManager){
  val notificatiochannel = NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
   enableLights(true)
   enableVibration(true)
   lightColor = ContextCompat.getColor(applicationContext, R.color.colorboton)
   lockscreenVisibility = Notification.VISIBILITY_PRIVATE
  }
  notificationManager.createNotificationChannel(notificatiochannel)
 }
}