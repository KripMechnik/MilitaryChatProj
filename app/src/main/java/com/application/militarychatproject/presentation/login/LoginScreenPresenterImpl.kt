package com.application.militarychatproject.presentation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.MENU_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.RESET_PASSWORD_SCREEN_ROUTE

class LoginScreenPresenterImpl(
    private val viewModel: LoginScreenViewModel,
    private val navController: NavController
) : LoginScreenPresenter {

    override val state = viewModel.state

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