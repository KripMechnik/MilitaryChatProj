package com.application.militarychatproject.presentation.messanger.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.MESSAGES_SCREEN_ROUTE

fun NavGraphBuilder.allChats(navController: NavController){

    composable(MESSAGES_SCREEN_ROUTE){
        AllChatsScreen()
    }
}