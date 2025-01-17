package com.application.timer_dmb.presentation.reset_password.reset

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.OTP_RESET_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class ResetScreenPresenterImpl (
    private val viewModel: ResetScreenViewModel,
    private val navController: NavController
): ResetScreenPresenter {

    override val getOtpResetState: StateFlow<GetOtpResetState?>
        get() = viewModel.getOtpResetState

    override fun navigateToOtpResetScreen(email: String) {
        navController.navigate(OTP_RESET_SCREEN_ROUTE + "/${email}")
    }

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun getOtpForResetPassword(email: String) {
        viewModel.getOtpForResetPassword(email)
    }

    override fun resetState() {
        viewModel.resetState()
    }

}