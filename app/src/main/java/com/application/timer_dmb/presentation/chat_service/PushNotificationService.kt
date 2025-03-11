package com.application.timer_dmb.presentation.chat_service

import android.util.Log
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
    private lateinit var sendFCMTokenUseCase: SendFCMTokenUseCase

    @Inject
    private lateinit var isAuthorizedUseCase: IsAuthorizedUseCase

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
        val title = message.data["title"] ?: "title"
        val content = message.data["content"] ?: "content"
        val imgUrl = message.data["url"] ?: ""

        Log.i("data_notification", "$title $content $imgUrl")
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}