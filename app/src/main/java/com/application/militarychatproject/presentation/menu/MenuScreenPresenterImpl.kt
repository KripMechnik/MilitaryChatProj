package com.application.militarychatproject.presentation.menu

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.REGISTRATION_SCREEN_ROUTE

class MenuScreenPresenterImpl(
    private val viewModel: MenuScreenViewModel,
    private val navController: NavController
) : MenuScreenPresenter{
    override fun navigateToRegister() {
        navController.navigate(REGISTRATION_SCREEN_ROUTE)
    }
}