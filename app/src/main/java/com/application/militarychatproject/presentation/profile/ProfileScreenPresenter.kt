package com.application.militarychatproject.presentation.profile

import kotlinx.coroutines.flow.StateFlow

interface ProfileScreenPresenter {

    val state: StateFlow<LogoutState?>

    fun logout()

    fun navigateToMenu()
}