package com.application.timer_dmb.presentation.messanger.all_chats

import kotlinx.coroutines.flow.StateFlow

interface AllChatsScreenPresenter {
    val chatsState: StateFlow<ChatsState?>

    val cleared: StateFlow<Boolean>

    fun navigateToChat(id: String)

    fun getChats()

    fun listenToSocket()

    fun close()

    fun onNavigating()
}