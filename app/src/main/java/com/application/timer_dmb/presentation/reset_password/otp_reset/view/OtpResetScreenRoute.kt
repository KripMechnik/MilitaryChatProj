package com.application.timer_dmb.presentation.reset_password.otp_reset.view

import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.timer_dmb.common.Constants.OTP_RESET_SCREEN_ROUTE
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.reset_password.otp_reset.OtpResetScreenPresenterImpl
import com.application.timer_dmb.presentation.reset_password.otp_reset.OtpResetScreenViewModel

fun NavGraphBuilder.otpReset(navController: NavController){

    composable("$OTP_RESET_SCREEN_ROUTE/{email}", listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = ""
        }
    )){

        val viewModel = hiltViewModel<OtpResetScreenViewModel>()

        val focusRequesters = remember {
            List(6) { FocusRequester() }
        }

        val presenter = OtpResetScreenPresenterImpl(viewModel, navController, focusRequesters)

        val email = it.arguments?.getString("email")

        OtpResetScreen(presenter, email ?: ""){ action ->
            when(action) {
                is OtpAction.OnEnterNumber -> {
                    if(action.number != null) {
                        focusRequesters[action.index].freeFocus()
                    }
                }
                else -> Unit
            }
            presenter.onAction(action)
        }
    }
}