package com.application.timer_dmb.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.TokenEntity
import com.application.timer_dmb.domain.entity.send.SignedInUserEntity
import com.application.timer_dmb.domain.usecases.authorization.SaveTokenUseCase
import com.application.timer_dmb.domain.usecases.authorization.SignInUseCase
import com.application.timer_dmb.domain.usecases.messages.SendFCMTokenUseCase
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val sendFCMTokenUseCase: SendFCMTokenUseCase,
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

                    Log.e("login", "success " + response.data!!.accessToken)
                    val data = response.data
                    var accessTokenString = data.accessToken.drop(7)
                    if (accessTokenString.startsWith("Bearer")) accessTokenString = accessTokenString.drop(7)

                    val savingToken = data.run {
                            saveTokenUseCase(accessTokenString, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt)
                        }
                    if (savingToken){
                        Log.i("success_saving", "Success")
                        sendFCMTokenUseCase(Firebase.messaging.token.await()).onEach{ result ->
                            when(result){
                                is Resource.Error -> Log.e("FCMError", result.code.toString() + " " + result.message)
                                is Resource.Loading -> {}
                                is Resource.Success -> Log.i("FCMSuccess", "Success")
                            }
                        }.launchIn(viewModelScope)
                        _state.value = LoginState.Success(response.data)
                    } else {
                        _state.value = LoginState.Error(message = "Didn't save token", code = null)
                    }
                }
                is Resource.Error -> {
                    _state.value = LoginState.Error(message = response.message ?: "Unknown error", code = response.code)
                    Log.e("login", "error " + (response.code.toString() + response.message))
                }
            }
        }.launchIn(viewModelScope)
    }
}

sealed class LoginState(val data: TokenEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: TokenEntity) : LoginState(data)
    class Error(message: String, code: Int?) : LoginState(message = message, code = code)
    class Loading() : LoginState()
}