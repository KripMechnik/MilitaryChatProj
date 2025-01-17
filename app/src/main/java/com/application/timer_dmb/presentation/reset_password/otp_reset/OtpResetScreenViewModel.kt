package com.application.timer_dmb.presentation.reset_password.otp_reset

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.SendOtpResetBodyEntity
import com.application.timer_dmb.domain.usecases.authorization.GetOtpForResetPasswordUseCase
import com.application.timer_dmb.domain.usecases.authorization.SendOtpForResetPasswordUseCase
import com.application.timer_dmb.presentation.registration.otp.GetCodeState
import com.application.timer_dmb.presentation.registration.otp.OtpAction
import com.application.timer_dmb.presentation.registration.otp.OtpState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OtpResetScreenViewModel @Inject constructor(
    private val getOtpForResetPasswordUseCase: GetOtpForResetPasswordUseCase,
    private val sendOtpForResetPasswordUseCase: SendOtpForResetPasswordUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val email = savedStateHandle.getStateFlow("email", "")

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _sendCodeState = MutableStateFlow<SendCodeResetState?>(null)
    val sendCodeState = _sendCodeState.asStateFlow()

    private val _getCodeState = MutableStateFlow<GetCodeState?>(null)
    val getCodeState = _getCodeState.asStateFlow()

    fun getCode(){
        val otpBody = GetOtpBodyEntity(email.value)
        getOtpForResetPasswordUseCase(otpBody).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _getCodeState.value = GetCodeState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("otp", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _getCodeState.value = GetCodeState.Loading()
                is Resource.Success -> _getCodeState.value = GetCodeState.Success(data = result.data!!)
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: OtpAction) {
        when(action) {
            is OtpAction.OnChangeFieldFocused -> {
                _state.update { it.copy(
                    focusedIndex = action.index
                ) }
            }
            is OtpAction.OnEnterNumber -> {
                enterNumber(action.number, action.index)
            }
            OtpAction.OnKeyboardBack -> {
                val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
                _state.update { it.copy(
                    code = it.code.mapIndexed { index, number ->
                        if(index == previousIndex) {
                            null
                        } else {
                            number
                        }
                    },
                    focusedIndex = previousIndex
                ) }
            }
        }
    }

    private fun enterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if(currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }
        val wasNumberRemoved = number == null
        _state.update { it ->
            it.copy(
                code = newCode,
                focusedIndex = if(wasNumberRemoved || it.code.getOrNull(index) != null) {
                    it.focusedIndex
                } else {
                    getNextFocusedTextFieldIndex(
                        currentCode = it.code,
                        currentFocusedIndex = it.focusedIndex
                    )
                },
                isValid = if(newCode.none { it == null }) {

                    val otpBody = SendOtpResetBodyEntity(email = email.value, otp = newCode.joinToString("").toInt())
                    sendOtpForResetPasswordUseCase(otpBody).onEach { result ->
                        when(result) {
                            is Resource.Error -> {
                                _sendCodeState.value = SendCodeResetState.Error(message = result.message ?: "Unknown error", code = result.code)
                                Log.e("otp", result.code.toString() + " " + result.message)
                            }
                            is Resource.Loading -> _sendCodeState.value = SendCodeResetState.Loading()
                            is Resource.Success -> {
                                _sendCodeState.value = SendCodeResetState.Success()
                            }
                        }
                    }.launchIn(viewModelScope)
                    _sendCodeState.value is SendCodeResetState.Success
                } else null
            ) }
    }

    private fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if(currentFocusedIndex == null) {
            return null
        }

        if(currentFocusedIndex == 5) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }

    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if(index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if(number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }
}

sealed class SendCodeResetState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success : SendCodeResetState()
    class Error(message: String, code: Int?) : SendCodeResetState(message = message, code = code)
    class Loading : SendCodeResetState()
}