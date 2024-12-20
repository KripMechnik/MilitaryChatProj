package com.application.militarychatproject.presentation.registration.registration

import kotlinx.coroutines.flow.StateFlow

interface RegistrationScreenPresenter {

    val state: StateFlow<RegistrationState?>

    fun navigateToOtp(email: String)

    fun navigateToLogin()

    fun signUp(nickname: String, password: String, email: String)
}