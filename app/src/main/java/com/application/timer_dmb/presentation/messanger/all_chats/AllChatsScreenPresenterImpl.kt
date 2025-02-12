package com.application.timer_dmb.presentation.messanger.all_chats

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.CHAT_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.MESSAGES_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class AllChatsScreenPresenterImpl(
    private val viewModel: AllChatsViewModel,
    private val navController: NavController
) : AllChatsScreenPresenter {

    override val chatsState: StateFlow<ChatsState?>
        get() = viewModel.chatsState
    override val cleared: StateFlow<Boolean>
        get() = viewModel.cleared

    override fun navigateToChat(id: String) {
        navController.navigate(CHAT_SCREEN_ROUTE +"/${id}"){
            popUpTo(MESSAGES_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }

    override fun getChats() {
        viewModel.getChats()
    }

    override fun listenToSocket() {
        viewModel.listenToSocket()
    }

    override fun close() {
        viewModel.close()
    }

    override fun onNavigating() {
        viewModel.onNavigating()
    }
}