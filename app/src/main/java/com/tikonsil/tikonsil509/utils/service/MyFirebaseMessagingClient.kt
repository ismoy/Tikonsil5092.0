package com.tikonsil.tikonsil509.utils.service

import android.content.Context
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.channel.NotificationHelper


/** * Created by ISMOY BELIZAIRE on 12/05/2022. */
class MyFirebaseMessagingClient : FirebaseMessagingService() {
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val data = message.data
        val title = data["title"]
        val body = data["body"]
        val idNotification = data["id_notification"]
        if (!title.isNullOrBlank() &&!body.isNullOrBlank() &&!idNotification.isNullOrBlank()){
            showNotification(title,body,idNotification)
        }

    }

    private fun showNotification(title:String,body:String,idNotification:String){

        val helper =NotificationHelper(baseContext)
        val builder =helper.getNotification(title, body)
        val id =idNotification.toInt()
        helper.getManager().notify(id,builder.build())
    }
}