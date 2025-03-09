package com.application.timer_dmb.presentation.chat_service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        //UpdateToken
    }


    override fun onMessageReceived(message: RemoteMessage) {
        //super.onMessageReceived(message)

        val title = message.data["title"] ?: "title"
        val content = message.data["content"] ?: "content"
        val imgUrl = message.data["url"] ?: ""

        Log.i("data_notification", "$title $content $imgUrl")
    }

}