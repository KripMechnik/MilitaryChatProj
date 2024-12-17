package com.application.militarychatproject.presentation.registration.registration

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.LOGIN_SCREEN_ROUTE

class RegistrationScreenPresenterImpl(
    private val viewModel: RegistrationScreenViewModel,
    private val navController: NavController
) : RegistrationScreenPresenter {
    override fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE)
    }

    override fun navigateToLogin() {
        navController.navigate(LOGIN_SCREEN_ROUTE)
    }
}