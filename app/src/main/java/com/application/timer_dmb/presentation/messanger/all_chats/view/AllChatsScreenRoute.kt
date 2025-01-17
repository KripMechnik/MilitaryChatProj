package com.application.timer_dmb.presentation.messanger.all_chats.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.timer_dmb.presentation.messanger.all_chats.AllChatsScreenPresenterImpl
import com.application.timer_dmb.presentation.messanger.all_chats.AllChatsViewModel

fun NavGraphBuilder.allChats(navController: NavController){

    composable(MESSAGES_SCREEN_ROUTE){

        val viewModel = hiltViewModel<AllChatsViewModel>()

        val presenter = AllChatsScreenPresenterImpl(viewModel, navController)

        AllChatsScreen(presenter)
    }
}