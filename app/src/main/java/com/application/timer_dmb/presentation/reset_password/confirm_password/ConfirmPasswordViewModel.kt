package com.application.timer_dmb.presentation.reset_password.confirm_password

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.ResetPasswordEntity
import com.application.timer_dmb.domain.usecases.authorization.ResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ConfirmPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    val email = savedStateHandle.getStateFlow("email", "")

    private val _resetPasswordState = MutableStateFlow<ResetPasswordState?>(null)
    val resetPasswordState = _resetPasswordState.asStateFlow()

    fun resetPassword(newPassword: String){
        val resetPasswordEntity = ResetPasswordEntity(password = newPassword, email = email.value)
        resetPasswordUseCase(resetPasswordEntity).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _resetPasswordState.value = ResetPasswordState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("otp", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _resetPasswordState.value = ResetPasswordState.Loading()
                is Resource.Success -> _resetPasswordState.value = ResetPasswordState.Success()
            }
        }.launchIn(viewModelScope)
    }
}

sealed class ResetPasswordState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : ResetPasswordState()
    class Error(message: String, code: Int?) : ResetPasswordState(message = message, code = code)
    class Loading : ResetPasswordState()
}