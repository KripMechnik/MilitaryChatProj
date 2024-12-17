package com.application.militarychatproject.presentation.registration.add_soldier

import kotlinx.coroutines.flow.MutableStateFlow

interface AddSoldierPresenter {

    fun navigateToHome()

    fun saveData(dateStart: String, dateEnd: String, name: String)
}