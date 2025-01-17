package com.application.timer_dmb.presentation.registration.otp.view

import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.timer_dmb.common.Constants.OTP_SCREEN_ROUTE
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.registration.otp.OtpScreenPresenterImpl
import com.application.timer_dmb.presentation.registration.otp.OtpViewModel

fun NavGraphBuilder.otp(navController: NavController){
    composable("$OTP_SCREEN_ROUTE/{email}", listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = ""
        }
    )){
        val viewModel: OtpViewModel = hiltViewModel<OtpViewModel>()
        val focusRequesters = remember {
            List(6) { FocusRequester() }
        }
        val presenter = OtpScreenPresenterImpl(viewModel, navController, focusRequesters)

        OtpScreen(presenter = presenter) { action ->
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