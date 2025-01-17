package com.application.timer_dmb.presentation.timer_settings

import kotlinx.coroutines.flow.StateFlow

interface TimerSettingsScreenPresenter {
    val timeState: StateFlow<SoldierTime>

    fun updateSoldierData()

    fun setSoldierStartState(dateStart: String)

    fun setSoldierEndState(dateEnd: String)

    fun navigateToMenu()
}