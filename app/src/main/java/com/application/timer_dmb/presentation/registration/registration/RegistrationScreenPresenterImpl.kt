package com.application.timer_dmb.presentation.registration.registration

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.OTP_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class RegistrationScreenPresenterImpl(
    private val viewModel: RegistrationScreenViewModel,
    private val navController: NavController
) : RegistrationScreenPresenter {
    override val state: StateFlow<RegistrationState?>
        get() = viewModel.state

    override fun resetState() {
        viewModel.resetState()
    }


    override fun navigateToOtp(email: String) {
        navController.navigate(OTP_SCREEN_ROUTE + "/${email}")
    }

    override fun navigateToLogin() {
        navController.navigate(LOGIN_SCREEN_ROUTE)
    }

    override fun signUp(nickname: String, password: String, email: String) {
        viewModel.signUp(nickname, password, email)
    }
}