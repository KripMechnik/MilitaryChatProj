package com.application.timer_dmb.presentation.reset_password.reset

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.usecases.authorization.GetOtpForResetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ResetScreenViewModel @Inject constructor(
    private val getOtpForResetPasswordUseCase: GetOtpForResetPasswordUseCase
): ViewModel() {

    private val _getOtpResetState = MutableStateFlow<GetOtpResetState?>(null)
    val getOtpResetState = _getOtpResetState.asStateFlow()

    fun getOtpForResetPassword(email: String) {
        val getOtpForResetEntity = GetOtpBodyEntity(email)

        getOtpForResetPasswordUseCase(getOtpForResetEntity).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _getOtpResetState.value = GetOtpResetState.Error(
                        message = result.message ?: "Unknown error",
                        code = result.code
                    )
                    Log.e("get_otp_reset_e", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _getOtpResetState.value = GetOtpResetState.Loading()
                is Resource.Success -> _getOtpResetState.value = GetOtpResetState.Success()
            }

        }.launchIn(viewModelScope)
    }

    fun resetState(){
        _getOtpResetState.value = null
    }
}

sealed class GetOtpResetState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : GetOtpResetState()
    class Error(message: String, code: Int?) : GetOtpResetState(message = message, code = code)
    class Loading : GetOtpResetState()
}