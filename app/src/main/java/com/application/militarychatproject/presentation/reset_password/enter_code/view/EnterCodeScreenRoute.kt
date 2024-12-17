package com.application.militarychatproject.presentation.reset_password.enter_code.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.ENTER_CODE_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.militarychatproject.presentation.login.view.LoginScreen

fun NavGraphBuilder.enterCode(navController: NavController){

    composable(ENTER_CODE_SCREEN_ROUTE){
        EnterCodeScreen()
    }
}