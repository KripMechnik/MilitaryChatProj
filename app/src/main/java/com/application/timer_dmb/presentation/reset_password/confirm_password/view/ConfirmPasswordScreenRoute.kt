package com.application.timer_dmb.presentation.reset_password.confirm_password.view

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.timer_dmb.common.Constants.CONFIRM_PASSWORD_SCREEN_ROUTE
import com.application.timer_dmb.presentation.reset_password.confirm_password.ConfirmPasswordPresenterImpl
import com.application.timer_dmb.presentation.reset_password.confirm_password.ConfirmPasswordViewModel

fun NavGraphBuilder.confirmPassword(navController: NavController){

    composable("$CONFIRM_PASSWORD_SCREEN_ROUTE/{email}", listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = ""
        }
    )){

        val viewModel = hiltViewModel<ConfirmPasswordViewModel>()

        val presenter = ConfirmPasswordPresenterImpl(viewModel, navController)

        ConfirmPasswordScreen(presenter)
    }
}