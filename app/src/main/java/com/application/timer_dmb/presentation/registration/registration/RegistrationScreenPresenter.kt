package com.application.timer_dmb.presentation.registration.registration

import kotlinx.coroutines.flow.StateFlow

interface RegistrationScreenPresenter {

    val state: StateFlow<RegistrationState?>

    fun resetState()

    fun navigateToOtp(email: String)

    fun navigateToLogin()

    fun signUp(nickname: String, password: String, email: String)
}