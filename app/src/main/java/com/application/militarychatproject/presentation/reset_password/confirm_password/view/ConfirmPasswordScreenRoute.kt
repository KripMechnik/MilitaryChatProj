package com.application.militarychatproject.presentation.reset_password.confirm_password.view

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.CONFIRM_PASSWORD_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.militarychatproject.presentation.login.view.LoginScreen

fun NavGraphBuilder.confirmPassword(navController: NavController){

    composable(CONFIRM_PASSWORD_SCREEN_ROUTE){
        ConfirmPasswordScreen()
    }
}