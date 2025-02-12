package com.application.timer_dmb.presentation.messanger.chat.view

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.application.timer_dmb.presentation.messanger.chat.ChatScreenViewModel

class ChatLifecycleObserver(private val viewModel: ChatScreenViewModel) : LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        // Приложение развернуто, подключаемся к вебсокету
        viewModel.listenToSocket()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        // Приложение свернуто, отключаемся от вебсокета
        viewModel.close()
    }
}