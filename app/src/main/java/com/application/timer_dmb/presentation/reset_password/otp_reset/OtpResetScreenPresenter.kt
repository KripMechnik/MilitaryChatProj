package com.application.timer_dmb.presentation.reset_password.otp_reset

import androidx.compose.ui.focus.FocusRequester
import com.application.timer_dmb.presentation.registration.otp.GetCodeState
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.registration.otp.OtpState
import kotlinx.coroutines.flow.StateFlow

interface OtpResetScreenPresenter {

    val state: StateFlow<OtpState>

    val focusRequesters: List<FocusRequester>

    val sendCodeState: StateFlow<SendCodeResetState?>

    val getCodeState: StateFlow<GetCodeState?>

    fun onAction(action: OtpAction)

    fun navigateUp()

    fun navigateToConfirmPassword(email: String)

    fun getCode()
}