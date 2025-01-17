package com.application.timer_dmb.presentation.login.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.timer_dmb.presentation.login.LoginScreenPresenterImpl
import com.application.timer_dmb.presentation.login.LoginScreenViewModel

fun NavGraphBuilder.login(navController: NavController){

    composable(LOGIN_SCREEN_ROUTE){

        val viewModel = hiltViewModel<LoginScreenViewModel>()

        val presenter = LoginScreenPresenterImpl(viewModel, navController)

        LoginScreen(presenter)
    }
}