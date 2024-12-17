package com.application.militarychatproject.presentation.login

import kotlinx.coroutines.flow.StateFlow

interface LoginScreenPresenter {

    val state: StateFlow<LoginState?>

    fun navigateToHome()

    fun navigateToReset()

    fun loginUser(eMail: String, password: String)
}