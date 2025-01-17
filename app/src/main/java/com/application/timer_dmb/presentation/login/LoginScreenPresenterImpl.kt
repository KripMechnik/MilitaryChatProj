package com.application.timer_dmb.presentation.login

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.MENU_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.RESET_PASSWORD_SCREEN_ROUTE

class LoginScreenPresenterImpl(
    private val viewModel: LoginScreenViewModel,
    private val navController: NavController
) : LoginScreenPresenter {

    override val state = viewModel.state
    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun navigateToHome() {
        navController.navigate(HOME_SCREEN_ROUTE){
            popUpTo(MENU_SCREEN_ROUTE) {
                inclusive = false
            }
        }
    }

    override fun navigateToReset() {
        navController.navigate(RESET_PASSWORD_SCREEN_ROUTE)
    }

    override fun loginUser(eMail: String, password: String) {
        viewModel.loginUser(eMail, password)
    }
}