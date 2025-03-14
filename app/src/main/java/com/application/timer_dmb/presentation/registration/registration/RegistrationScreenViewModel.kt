package com.application.timer_dmb.presentation.registration.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.NewUserEntity
import com.application.timer_dmb.domain.usecases.authorization.RegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<RegistrationState?>(null)
    val state = _state.asStateFlow()

    fun signUp(nickname: String, password: String, email: String){
        val newUserEntity = NewUserEntity(email, password, nickname)
        registrationUseCase(newUserEntity).onEach { result ->
            when (result){
                is Resource.Error -> {
                    _state.value = RegistrationState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("registration", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _state.value = RegistrationState.Loading()
                is Resource.Success -> _state.value = RegistrationState.Success(data = Unit)
            }
        }.launchIn(viewModelScope)
    }

    fun resetState(){
        _state.value = null
    }

}

sealed class RegistrationState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : RegistrationState(data)
    class Error(message: String, code: Int?) : RegistrationState(message = message, code = code)
    class Loading : RegistrationState()
}