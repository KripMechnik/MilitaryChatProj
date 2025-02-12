package com.application.timer_dmb.presentation.menu

import android.graphics.Bitmap
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.CALENDAR_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.PROFILE_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.REGISTRATION_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.SETTINGS_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class MenuScreenPresenterImpl(
    private val viewModel: MenuScreenViewModel,
    private val navController: NavController
) : MenuScreenPresenter {

    override val state: StateFlow<MenuState?>
        get() = viewModel.state

    override val registered: StateFlow<Boolean>
        get() = viewModel.registered

    override fun logoutWhenNoConnection() {
        viewModel.logoutWhenNoConnection()
        navController.popBackStack()
    }

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

    override fun navigateToSettings() {
        navController.navigate(SETTINGS_SCREEN_ROUTE)
    }

    override fun saveBitmapAsFile(bitmap: Bitmap) {
        viewModel.saveBitmapAsFile(bitmap)
    }
}