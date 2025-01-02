package com.application.militarychatproject.presentation.messanger.chat

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.CHAT_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.militarychatproject.domain.entity.receive.MessageEntity
import io.ktor.websocket.Frame
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

    override val lastMessages: StateFlow<Boolean>
        get() = viewModel.lastMessages
    override val sendState: StateFlow<SendState?>
        get() = viewModel.sendState

    override fun navigateUp() {
        navController.navigate(MESSAGES_SCREEN_ROUTE){
            popUpTo(CHAT_SCREEN_ROUTE){
                inclusive = true
            }
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
}