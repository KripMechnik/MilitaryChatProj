package com.application.timer_dmb.presentation.registration.otp

import androidx.compose.ui.focus.FocusRequester
import kotlinx.coroutines.flow.StateFlow

interface OtpScreenPresenter {

    val state: StateFlow<OtpState>

    val sendCodeState: StateFlow<SendCodeState?>

    val getCodeState: StateFlow<GetCodeState?>

    val focusRequesters: List<FocusRequester>

    fun navigateUp()

    fun onAction(action: OtpAction)

    fun getCode()

    fun navigateToSetPhoto()
}