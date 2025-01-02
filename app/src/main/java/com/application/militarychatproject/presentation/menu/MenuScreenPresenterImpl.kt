package com.application.militarychatproject.presentation.menu

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.PROFILE_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.REGISTRATION_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class MenuScreenPresenterImpl(
    private val viewModel: MenuScreenViewModel,
    private val navController: NavController
) : MenuScreenPresenter {

    override val state: StateFlow<MenuState?>
        get() = viewModel.state

    override val registered: StateFlow<Boolean>
        get() = viewModel.registered

    override fun navigateToRegister() {
        navController.navigate(REGISTRATION_SCREEN_ROUTE)
    }

    override fun navigateToProfile() {
        navController.navigate(PROFILE_SCREEN_ROUTE)
    }

    override fun navigateToCalendar() {
        navController.navigate(CALENDAR_SCREEN_ROUTE)
    }

    override fun checkAuthorized() {
        viewModel.checkAuthorized()
    }

    override fun getSelfUser() {
        viewModel.getSelfUser()
    }
}