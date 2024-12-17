package com.application.militarychatproject.presentation.reset_password.reset.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.RESET_PASSWORD_SCREEN_ROUTE
import com.application.militarychatproject.presentation.login.view.LoginScreen

fun NavGraphBuilder.reset(navController: NavController){

    composable(RESET_PASSWORD_SCREEN_ROUTE){
        ResetScreen()
    }
}