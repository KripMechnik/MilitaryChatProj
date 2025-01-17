package com.application.timer_dmb.presentation.reset_password.confirm_password

import kotlinx.coroutines.flow.StateFlow

interface ConfirmPasswordPresenter {

    val resetPasswordState: StateFlow<ResetPasswordState?>

    fun resetPassword(newPassword: String)

    fun navigateUp()

    fun navigateToHome()
}