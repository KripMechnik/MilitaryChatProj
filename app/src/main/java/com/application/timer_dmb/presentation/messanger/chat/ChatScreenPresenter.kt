package com.application.timer_dmb.presentation.messanger.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import com.application.timer_dmb.domain.entity.receive.MessageEntity
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.profile.ProfileState
import kotlinx.coroutines.flow.StateFlow

interface ChatScreenPresenter {

    val messagesState: StateFlow<MessagesState?>

    val listState: SnapshotStateList<MessageEntity>

    val singleMessageState: StateFlow<SingleMessageState?>

    val replyToId: StateFlow<String>

    val authorized: StateFlow<Boolean>

    val lastMessages: StateFlow<Boolean>

    val sendState: StateFlow<SendState?>

    val state: StateFlow<HomeState>

    val profileState: StateFlow<ProfileState?>

    val shareImageBitmap: StateFlow<ImageBitmap?>

    val backgroundState: StateFlow<BackgroundState?>

    fun navigateToRegister()

    fun setBitmap(bitmap: ImageBitmap)

    fun banUser(userId: String)

    fun navigateUp()

    fun getMessages()

    fun sendMessage(text: String)

    fun setReplyToId(newId: String)

    fun updateMessage(newText: String)

    fun deleteMessage(messageId: String)

    fun listenToSocket()

    fun close()

    fun sendTimer()

}