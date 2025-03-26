package com.application.timer_dmb.presentation.chat_service

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.messages.SendFCMTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var sendFCMTokenUseCase: SendFCMTokenUseCase

    @Inject
    lateinit var isAuthorizedUseCase: IsAuthorizedUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (isAuthorizedUseCase()){
            sendFCMTokenUseCase(token).onEach { result ->
                when(result) {
                    is Resource.Error -> Log.e("FCMToken", "${result.code} ${result.message}")
                    is Resource.Loading -> {}
                    is Resource.Success -> Log.i("FCMToken", "success")
                }
            }.launchIn(scope)
        }
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
//        val title = message.data["title"] ?: "title"
//        val content = message.data["content"] ?: "content"
//        val imgUrl = message.data["url"] ?: ""
//
//        Log.i("data_notification", "$title $content $imgUrl")
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    @SuppressLint("ServiceCast")
    private fun showCustomNotification(title: String, body: String) {
        // Создаем RemoteViews для кастомного макета
//        val notificationLayout = RemoteViews(packageName, R.layout.custom_notification)
//        notificationLayout.setTextViewText(R.id.notification_title, title)
//        notificationLayout.setTextViewText(R.id.notification_body, body)

        // Создаем канал уведомлений (для Android 8.0 и выше)
        val channelId = "custom_notification_channel"

        // Создаем уведомление
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            //.setSmallIcon(R.drawable.ic_notification)
            //.setCustomContentView(notificationLayout)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        // Показываем уведомление
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notificationBuilder.build())
    }
}