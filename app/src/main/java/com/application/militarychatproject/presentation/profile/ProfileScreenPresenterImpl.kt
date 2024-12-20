package com.application.militarychatproject.presentation.profile

import androidx.navigation.NavController
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenPresenterImpl(
    private val viewModel: ProfileScreenViewModel,
    private val navController: NavController
) : ProfileScreenPresenter {
    override val state: StateFlow<LogoutState?>
        get() = viewModel.state

    override fun logout() {
        viewModel.logout()
    }

    override fun navigateToMenu() {
        navController.navigateUp()
    }
}