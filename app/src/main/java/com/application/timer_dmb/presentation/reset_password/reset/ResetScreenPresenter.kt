package com.application.timer_dmb.presentation.reset_password.reset

import kotlinx.coroutines.flow.StateFlow

interface ResetScreenPresenter {

    val getOtpResetState: StateFlow<GetOtpResetState?>

    fun navigateToOtpResetScreen(email: String)

    fun navigateUp()

    fun getOtpForResetPassword(email: String)

    fun resetState()
}