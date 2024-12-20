package com.application.militarychatproject.presentation.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.usecases.authorization.DeleteTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<LogoutState?>(null)
    val state = _state.asStateFlow()

    fun logout(){
        logoutUseCase().onEach {result ->
            when(result){
                is Resource.Error -> {
                    _state.value = LogoutState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("profile", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _state.value = LogoutState.Loading()
                is Resource.Success -> {
                    deleteTokenUseCase()
                    _state.value = LogoutState.Success(data = Unit)
                }
            }
        }.launchIn(viewModelScope)
    }

}

sealed class LogoutState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : LogoutState(data)
    class Error(message: String, code: Int?) : LogoutState(message = message, code = code)
    class Loading : LogoutState()
}