package com.application.militarychatproject.presentation.messanger.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.application.militarychatproject.domain.entity.receive.MessageEntity
import kotlinx.coroutines.flow.StateFlow

interface ChatScreenPresenter {

    val messagesState: StateFlow<MessagesState?>

    val listState: SnapshotStateList<MessageEntity>

    val singleMessageState: StateFlow<SingleMessageState?>

    val replyToId: StateFlow<String>

    val lastMessages: StateFlow<Boolean>

    val sendState: StateFlow<SendState?>

    fun navigateUp()

    fun getMessages()

    fun sendMessage(text: String)

    fun setReplyToId(newId: String)

    fun updateMessage(newText: String)

    fun deleteMessage(messageId: String)

}