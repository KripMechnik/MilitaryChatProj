package com.application.militarychatproject.presentation.messanger.all_chats

import kotlinx.coroutines.flow.StateFlow

interface AllChatsScreenPresenter {
    val chatsState: StateFlow<ChatsState?>

    val cleared: StateFlow<Boolean>

    fun navigateToChat(id: String)

    fun getChats()

    fun onNavigating()
}