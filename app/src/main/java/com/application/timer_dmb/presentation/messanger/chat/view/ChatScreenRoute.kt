package com.application.timer_dmb.presentation.messanger.chat.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.timer_dmb.common.Constants.CHAT_SCREEN_ROUTE
import com.application.timer_dmb.presentation.messanger.chat.ChatScreenPresenterImpl
import com.application.timer_dmb.presentation.messanger.chat.ChatScreenViewModel

fun NavGraphBuilder.chat(navController: NavController){
    composable("$CHAT_SCREEN_ROUTE/{id}", listOf(
        navArgument("id") {
            type = NavType.StringType
            defaultValue = ""
        }
    )){


        val viewModel = hiltViewModel<ChatScreenViewModel>()

        val presenter = ChatScreenPresenterImpl(viewModel, navController)

        ChatScreen(presenter)
    }
}