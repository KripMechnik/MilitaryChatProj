package com.application.timer_dmb.presentation.timer_settings

import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

class TimerSettingsScreenPresenterImpl(
    private val viewModel: TimerSettingsScreenViewModel,
    private val navController: NavController
) : TimerSettingsScreenPresenter {
    override val timeState: StateFlow<SoldierTime>
        get() = viewModel.timeState

    override fun updateSoldierData() {
        viewModel.updateSoldierData()
    }

    override fun setSoldierStartState(dateStart: String) {
        viewModel.setSoldierStartState(dateStart)
    }

    override fun setSoldierEndState(dateEnd: String) {
        viewModel.setSoldierEndState(dateEnd)
    }

    override fun navigateToMenu() {
        navController.popBackStack()
    }


}