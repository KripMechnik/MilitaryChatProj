package com.application.timer_dmb.presentation.choose_type

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.send.UserTypeEntity
import com.application.timer_dmb.domain.usecases.user.SetUserTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TypeScreenViewModel @Inject constructor(
    private val setUserTypeUseCase: SetUserTypeUseCase
): ViewModel() {

    private val _setTypeState = MutableStateFlow<SetTypeState?>(null)
    val setTypeState = _setTypeState.asStateFlow()

    fun setType(type: String){
        val setTypeEntity = UserTypeEntity(type)
        setUserTypeUseCase(setTypeEntity).onEach { response ->
            when (response) {
                is Resource.Loading -> {
                    _setTypeState.value = SetTypeState.Loading()
                    Log.e("login", "loading")
                }
                is Resource.Success -> {
                    _setTypeState.value = SetTypeState.Success()
                }
                is Resource.Error -> {
                    _setTypeState.value = SetTypeState.Error(message = response.message ?: "Unknown error", code = response.code)
                    Log.e("login", "error " + (response.code.toString() + response.message))
                }
            }
        }.launchIn(viewModelScope)
    }

}

sealed class SetTypeState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success() : SetTypeState()
    class Error(message: String, code: Int?) : SetTypeState(message = message, code = code)
    class Loading() : SetTypeState()
}