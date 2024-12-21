package com.application.militarychatproject.presentation.profile

import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import com.application.militarychatproject.domain.usecases.authorization.DeleteTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.LogoutUseCase
import com.application.militarychatproject.domain.usecases.user.GetSelfUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase
) : ViewModel(){



    private val _state = MutableStateFlow<LogoutState?>(null)
    val state = _state.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    private val _cropState = MutableStateFlow<ResultCropState?>(null)
    val cropState = _cropState.asStateFlow()

    init {
        getSelfUser()
    }

    fun setCropState(bitmap: ImageBitmap){
        _cropState.value = ResultCropState.Success(data = bitmap)
    }

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

    private fun getSelfUser(){
        getSelfUserDataUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _profileState.value = ProfileState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("menu", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _profileState.value = ProfileState.Loading()
                is Resource.Success -> _profileState.value = ProfileState.Success(data = result.data!!)
            }
        }.launchIn(viewModelScope)
    }

}

sealed class LogoutState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : LogoutState(data)
    class Error(message: String, code: Int?) : LogoutState(message = message, code = code)
    class Loading : LogoutState()
}

sealed class ResultCropState(val data: ImageBitmap? = null, val message: String? = null){
    class Success(data: ImageBitmap) : ResultCropState(data = data)
    class Error(message: String) : ResultCropState(message = message)
    class Loading : ResultCropState()
}

sealed class ProfileState(val data: SelfUserEntity? = null, val message: String? = null, code: Int? = null){
    class Success(data: SelfUserEntity) : ProfileState(data = data)
    class Error(message: String, code: Int?) : ProfileState(message = message, code = code)
    class Loading : ProfileState()
}
