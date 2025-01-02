package com.application.militarychatproject.presentation.messanger.all_chats

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.CHAT_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MESSAGES_SCREEN_ROUTE
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

    override fun onNavigating() {
        viewModel.onNavigating()
    }
}