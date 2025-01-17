package com.application.timer_dmb.presentation.registration.add_soldier

import kotlinx.coroutines.flow.StateFlow

interface AddSoldierScreenPresenter {

    val addedEvents: StateFlow<Boolean>

    fun navigateToHome()

    fun saveData(dateStart: String, dateEnd: String, name: String)
}