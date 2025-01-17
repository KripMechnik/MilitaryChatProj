package com.application.timer_dmb.presentation.settings

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.TIMER_SETTINGS_SCREEN_ROUTE

class SettingsScreenPresenterImpl(
    private val viewModel: SettingsScreenViewModel,
    private val navController: NavController
) : SettingsScreenPresenter {
    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun navigateToTimerSettings() {
        navController.navigate(TIMER_SETTINGS_SCREEN_ROUTE)
    }

    override fun resetBackground() {
        viewModel.resetBackground()
    }
}