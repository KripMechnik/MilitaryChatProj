package com.application.militarychatproject.presentation.registration.otp

import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MENU_SCREEN_ROUTE
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


    override fun onAction(action: OtpAction) {
        viewModel.onAction(action)
    }

    override fun getCode() {
        viewModel.getCode()
    }

    override fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE){
            popUpTo(MENU_SCREEN_ROUTE)
        }
    }
}