package com.application.militarychatproject.presentation.messanger.all_chats.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.MESSAGES_SCREEN_ROUTE
import com.application.militarychatproject.presentation.messanger.all_chats.AllChatsScreenPresenterImpl
import com.application.militarychatproject.presentation.messanger.all_chats.AllChatsViewModel

fun NavGraphBuilder.allChats(navController: NavController){

    composable(MESSAGES_SCREEN_ROUTE){

        val viewModel = hiltViewModel<AllChatsViewModel>()

        val presenter = AllChatsScreenPresenterImpl(viewModel, navController)

        AllChatsScreen(presenter)
    }
}