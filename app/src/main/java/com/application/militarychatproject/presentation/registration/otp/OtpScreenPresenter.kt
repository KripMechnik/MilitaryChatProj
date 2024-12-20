package com.application.militarychatproject.presentation.registration.otp

import androidx.compose.ui.focus.FocusRequester
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

interface OtpScreenPresenter {

    val state: StateFlow<OtpState>

    val sendCodeState: StateFlow<SendCodeState?>

    val getCodeState: StateFlow<GetCodeState?>

    val focusRequesters: List<FocusRequester>

    fun onAction(action: OtpAction)

    fun getCode()

    fun navigateToHome()
}