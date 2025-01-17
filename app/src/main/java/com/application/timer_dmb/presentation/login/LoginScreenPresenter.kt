package com.application.timer_dmb.presentation.login

import kotlinx.coroutines.flow.StateFlow

interface LoginScreenPresenter {

    val state: StateFlow<LoginState?>

    fun navigateUp()

    fun navigateToHome()

    fun navigateToReset()

    fun loginUser(eMail: String, password: String)
}