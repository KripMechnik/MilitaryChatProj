package com.application.timer_dmb.presentation.reset_password.reset.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.application.timer_dmb.common.Constants.RESET_PASSWORD_SCREEN_ROUTE
import com.application.timer_dmb.presentation.reset_password.reset.ResetScreenPresenterImpl
import com.application.timer_dmb.presentation.reset_password.reset.ResetScreenViewModel

fun NavGraphBuilder.reset(navController: NavController){

    composable(RESET_PASSWORD_SCREEN_ROUTE){
        val viewModel = hiltViewModel<ResetScreenViewModel>()
        val presenter = ResetScreenPresenterImpl(viewModel, navController)
        ResetScreen(presenter)
    }
}