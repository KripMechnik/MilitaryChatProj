package com.application.militarychatproject.presentation.registration.otp

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.domain.entity.receive.TokenEntity
import com.application.militarychatproject.domain.entity.send.GetOtpBodyEntity
import com.application.militarychatproject.domain.entity.send.SendOtpBodyEntity
import com.application.militarychatproject.domain.usecases.authorization.GetOtpCodeUseCase
import com.application.militarychatproject.domain.usecases.authorization.SaveTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.SendOtpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class OtpViewModel @Inject constructor(
    private val getOtpCodeUseCase: GetOtpCodeUseCase,
    private val sendOtpUseCase: SendOtpUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val email = savedStateHandle.getStateFlow("email", "")

    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _getCodeState = MutableStateFlow<GetCodeState?>(null)
    val getCodeState = _getCodeState.asStateFlow()

    private val _sendCodeState = MutableStateFlow<SendCodeState?>(null)
    val sendCodeState = _sendCodeState.asStateFlow()

    init {
        getCode()
    }

    fun getCode(){
        Log.e("otp", email.value)
        val otpBody = GetOtpBodyEntity(email.value)
        getOtpCodeUseCase(otpBody).onEach { result ->
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
                val otpBody = SendOtpBodyEntity(email.value, newCode.joinToString(""))
                sendOtpUseCase(otpBody).onEach { result ->
                    when(result) {
                        is Resource.Error -> {
                            _sendCodeState.value = SendCodeState.Error(message = result.message ?: "Unknown error", code = result.code)
                            Log.e("otp", result.code.toString() + " " + result.message)
                        }
                        is Resource.Loading -> _sendCodeState.value = SendCodeState.Loading()
                        is Resource.Success -> {
                            _sendCodeState.value = SendCodeState.Success(data = result.data!!)
                            result.data.apply {
                                saveTokenUseCase(accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt)
                            }

                        }
                    }
                }.launchIn(viewModelScope)
                _sendCodeState.value is SendCodeState.Success
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

sealed class GetCodeState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : GetCodeState(data)
    class Error(message: String, code: Int?) : GetCodeState(message = message, code = code)
    class Loading : GetCodeState()
}

sealed class SendCodeState(val data: TokenEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: TokenEntity) : SendCodeState(data)
    class Error(message: String, code: Int?) : SendCodeState(message = message, code = code)
    class Loading : SendCodeState()
}

data class OtpState(
    val code: List<Int?> = (1..6).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null
)

sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int): OtpAction
    data class OnChangeFieldFocused(val index: Int): OtpAction
    data object OnKeyboardBack: OtpAction
}