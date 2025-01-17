package com.application.timer_dmb.presentation.registration.otp

import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.OTP_RESET_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.SET_PHOTO_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class OtpScreenPresenterImpl(
    private val viewModel: OtpViewModel,
    private val navController: NavController,
    override val focusRequesters: List<FocusRequester>
) : OtpScreenPresenter {
    override val state: StateFlow<OtpState>
        get() = viewModel.state
    override val sendCodeState: StateFlow<SendCodeState?>
        get() = viewModel.sendCodeState
    override val getCodeState: StateFlow<GetCodeState?>
        get() = viewModel.getCodeState

    override fun navigateUp() {
        navController.popBackStack()
    }


    override fun onAction(action: OtpAction) {
        viewModel.onAction(action)
    }

    override fun getCode() {
        viewModel.getCode()
    }

    override fun navigateToSetPhoto() {
        navController.navigate(SET_PHOTO_SCREEN_ROUTE){
            popUpTo(OTP_RESET_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }
}