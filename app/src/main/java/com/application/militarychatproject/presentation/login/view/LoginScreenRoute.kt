package com.application.militarychatproject.presentation.login.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.militarychatproject.presentation.login.LoginScreenPresenterImpl
import com.application.militarychatproject.presentation.login.LoginScreenViewModel

fun NavGraphBuilder.login(navController: NavController){

    composable(LOGIN_SCREEN_ROUTE){

        val viewModel = hiltViewModel<LoginScreenViewModel>()

        val presenter = LoginScreenPresenterImpl(viewModel, navController)

        LoginScreen(presenter)
    }
}