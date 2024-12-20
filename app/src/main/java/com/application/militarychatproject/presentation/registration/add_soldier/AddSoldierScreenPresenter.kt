package com.application.militarychatproject.presentation.registration.add_soldier

interface AddSoldierScreenPresenter {

    fun navigateToHome()

    fun saveData(dateStart: String, dateEnd: String, name: String)
}