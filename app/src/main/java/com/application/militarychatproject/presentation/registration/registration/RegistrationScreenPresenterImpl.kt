package com.application.militarychatproject.presentation.registration.registration

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.ENTER_CODE_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class RegistrationScreenPresenterImpl(
    private val viewModel: RegistrationScreenViewModel,
    private val navController: NavController
) : RegistrationScreenPresenter {
    override val state: StateFlow<RegistrationState?>
        get() = viewModel.state


    override fun navigateToOtp(email: String) {
        navController.navigate(ENTER_CODE_SCREEN_ROUTE + "/${email}")
    }

    override fun navigateToLogin() {
        navController.navigate(LOGIN_SCREEN_ROUTE)
    }

    override fun signUp(nickname: String, password: String, email: String) {
        viewModel.signUp(nickname, password, email)
    }
}