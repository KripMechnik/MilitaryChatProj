package com.application.timer_dmb.presentation.reset_password.otp_reset

import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.CONFIRM_PASSWORD_SCREEN_ROUTE
import com.application.timer_dmb.presentation.registration.otp.GetCodeState
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.registration.otp.OtpState
import kotlinx.coroutines.flow.StateFlow

class OtpResetScreenPresenterImpl(
    val viewModel: OtpResetScreenViewModel,
    val navController: NavController,
    override val focusRequesters: List<FocusRequester>
): OtpResetScreenPresenter {

    override val state: StateFlow<OtpState>
        get() = viewModel.state

    override val sendCodeState: StateFlow<SendCodeResetState?>
        get() = viewModel.sendCodeState

    override val getCodeState: StateFlow<GetCodeState?>
        get() = viewModel.getCodeState

    override fun onAction(action: OtpAction) {
        viewModel.onAction(action)
    }

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun navigateToConfirmPassword(email: String) {
        navController.navigate("$CONFIRM_PASSWORD_SCREEN_ROUTE/$email")
    }

    override fun getCode() {
        viewModel.getCode()
    }

}