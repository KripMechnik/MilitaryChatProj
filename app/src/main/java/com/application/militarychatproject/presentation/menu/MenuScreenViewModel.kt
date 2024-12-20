package com.application.militarychatproject.presentation.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import com.application.militarychatproject.domain.entity.receive.UserEntity
import com.application.militarychatproject.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.militarychatproject.domain.usecases.user.GetSelfUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase
): ViewModel() {

    private val _registered = MutableStateFlow(false)
    val registered = _registered.asStateFlow()

    private val _state = MutableStateFlow<MenuState?>(null)
    val state = _state.asStateFlow()



    init {
        if (_registered.value){
            getSelfUser()
        }
    }

    fun checkAuthorized() {
        _registered.value = isAuthorizedUseCase()
    }
    private fun getSelfUser(){
        getSelfUserDataUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _state.value = MenuState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("menu", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _state.value = MenuState.Loading()
                is Resource.Success -> _state.value = MenuState.Success(data = result.data!!)
            }
        }.launchIn(viewModelScope)
    }
}

sealed class MenuState(val data: SelfUserEntity? = null, val message: String? = null, code: Int? = null){
    class Success(data: SelfUserEntity) : MenuState(data = data)
    class Error(message: String, code: Int?) : MenuState(message = message, code = code)
    class Loading : MenuState()
}