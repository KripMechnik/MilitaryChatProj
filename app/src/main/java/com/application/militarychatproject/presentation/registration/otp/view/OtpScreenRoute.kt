package com.application.militarychatproject.presentation.registration.otp.view

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.application.militarychatproject.common.Constants.ENTER_CODE_SCREEN_ROUTE
import com.application.militarychatproject.presentation.registration.otp.OtpAction
import com.application.militarychatproject.presentation.registration.otp.OtpScreenPresenterImpl
import com.application.militarychatproject.presentation.registration.otp.OtpViewModel

fun NavGraphBuilder.otp(navController: NavController){
    composable("$ENTER_CODE_SCREEN_ROUTE/{email}", listOf(
        navArgument("name") {
            type = NavType.StringType
            defaultValue = ""
        }
    )){ entry ->

        val viewModel: OtpViewModel = hiltViewModel<OtpViewModel>()
        val focusRequesters = remember {
            List(6) { FocusRequester() }
        }
        val presenter = OtpScreenPresenterImpl(viewModel, navController, focusRequesters)

        OtpScreen(presenter = presenter, email = entry.arguments?.getString("email") ?: "") { action ->
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