package com.application.militarychatproject.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity
import com.application.militarychatproject.domain.usecases.authorization.SignInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState?>(null)
    val state = _state.asStateFlow()

    fun loginUser(eMail: String, password: String){
        val signedInUserEntity = SignedInUserEntity(eMail, password)
        signInUseCase(signedInUserEntity).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _state.value = LoginState.Loading()
                    Log.e("login", "loading")
                }
                is Resource.Success -> {
                    _state.value = LoginState.Success(response.data!!)
                    Log.e("login", "success " + response.data.accessToken)
                }
                is Resource.Error -> {
                    _state.value = LoginState.Error(message = response.message ?: "Unknown error")
                    Log.e("login", "error " + (response.code.toString() + response.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}

sealed class LoginState(val data: TokenDTO? = null, val message: String? = null){
    class Success(data: TokenDTO) : LoginState(data)
    class Error(message: String) : LoginState(message = message)
    class Loading() : LoginState()
}