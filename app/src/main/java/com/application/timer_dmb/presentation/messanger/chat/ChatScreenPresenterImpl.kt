package com.application.timer_dmb.presentation.messanger.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.REGISTRATION_SCREEN_ROUTE
import com.application.timer_dmb.domain.entity.receive.MessageEntity
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.profile.ProfileState
import kotlinx.coroutines.flow.StateFlow

class ChatScreenPresenterImpl(
    private val viewModel: ChatScreenViewModel,
    private val navController: NavController
) : ChatScreenPresenter {

    override val messagesState: StateFlow<MessagesState?>
        get() = viewModel.messagesState

    override val listState: SnapshotStateList<MessageEntity>
        get() = viewModel.listState

    override val singleMessageState: StateFlow<SingleMessageState?>
        get() = viewModel.singleMessageState

    override val replyToId: StateFlow<String>
        get() = viewModel.replyToId

    override val authorized: StateFlow<Boolean>
        get() = viewModel.authorized

    override val lastMessages: StateFlow<Boolean>
        get() = viewModel.lastMessages

    override val sendState: StateFlow<SendState?>
        get() = viewModel.sendState

    override val state: StateFlow<HomeState>
        get() = viewModel.state

    override val profileState: StateFlow<ProfileState?>
        get() = viewModel.profileState

    override val shareImageBitmap: StateFlow<ImageBitmap?>
        get() = viewModel.shareImageBitmap

    override val backgroundState: StateFlow<BackgroundState?>
        get() = viewModel.background

    override fun navigateToRegister() {
        navController.navigate(REGISTRATION_SCREEN_ROUTE)
    }

    override fun setBitmap(bitmap: ImageBitmap) {
        viewModel.setBitmap(bitmap)
    }

    override fun banUser(userId: String) {
        viewModel.banUser(userId)
    }

    override fun navigateUp() {

        navController.navigate(MESSAGES_SCREEN_ROUTE){
            navController.popBackStack()
        }
    }

    override fun getMessages() {
        viewModel.getChatMessages()
    }

    override fun sendMessage(text: String) {
        viewModel.sendMessage(text)
    }

    override fun setReplyToId(newId: String) {
        viewModel.setReplyToId(newId)
    }

    override fun updateMessage(newText: String) {
        viewModel.updateMessage(newText)
    }

    override fun deleteMessage(messageId: String) {
        viewModel.deleteMessage(messageId)
    }

    override fun listenToSocket() {
        viewModel.listenToSocket()
    }

    override fun close() {
        viewModel.close()
    }

    override fun sendTimer() {
        viewModel.sendTimer()
    }
}