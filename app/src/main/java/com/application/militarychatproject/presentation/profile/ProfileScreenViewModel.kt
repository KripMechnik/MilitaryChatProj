package com.application.militarychatproject.presentation.profile

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.domain.entity.receive.PhotoEntity
import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import com.application.militarychatproject.domain.usecases.authorization.DeleteAccUseCase
import com.application.militarychatproject.domain.usecases.authorization.DeleteTokenUseCase
import com.application.militarychatproject.domain.usecases.authorization.LogoutUseCase
import com.application.militarychatproject.domain.usecases.user.GetPhotoUseCase
import com.application.militarychatproject.domain.usecases.user.GetSelfUserDataUseCase
import com.application.militarychatproject.domain.usecases.user.SavePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase,
    private val savePhotoUseCase: SavePhotoUseCase,
    private val getPhotoUseCase: GetPhotoUseCase,
    private val deleteAccUseCase: DeleteAccUseCase
) : ViewModel(){



    private val _state = MutableStateFlow<LogoutState?>(null)
    val state = _state.asStateFlow()

    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    private val _cropState = MutableStateFlow<ResultCropState?>(null)
    val cropState = _cropState.asStateFlow()

    private val _sendCropState = MutableStateFlow<SendCropState?>(null)
    val sendCropState = _sendCropState.asStateFlow()

    private val _deleteAccState = MutableStateFlow<DeleteAccState?>(null)
    val deleteAccState = _deleteAccState.asStateFlow()

    init {
        getSelfUser()
    }

    fun getPhoto(){
        getPhotoUseCase().onEach { result ->
            when (result){
                is Resource.Error -> Log.e("profile", result.code.toString() + " " + result.message)
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (_profileState.value is ProfileState.Success){
                        _profileState.value = ProfileState.Success(data = _profileState.value!!.data!!.copy(avatarLink = result.data!!.avatarLink))
                    }
                }
            }
        }.launchIn(viewModelScope)
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

    fun sendImage(){
        viewModelScope.launch {
            val image = async(Dispatchers.Default) {
                toByteArray()
            }.await()

            savePhotoUseCase(image).collect{ result ->
                when (result) {
                    is Resource.Error -> {
                        _sendCropState.value = SendCropState.Error(message = result.message ?: "Unknown error", code = result.code)
                        Log.e("menu_send", result.code.toString() + " " + result.message)
                    }
                    is Resource.Loading -> _sendCropState.value = SendCropState.Loading()
                    is Resource.Success -> _sendCropState.value = SendCropState.Success(Unit)
                }
            }
        }
    }

    private fun toByteArray(): ByteArray{
        val stream = ByteArrayOutputStream()
        _cropState.value?.data?.asAndroidBitmap()?.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        stream.close()
        return image
    }

    fun delete(){
        deleteAccUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _deleteAccState.value = DeleteAccState.Error(message = result.message ?: "Unknown error", code = result.code)
                    Log.e("menu_send", result.code.toString() + " " + result.message)
                }
                is Resource.Loading -> _deleteAccState.value = DeleteAccState.Loading()
                is Resource.Success -> {
                    _deleteAccState.value = DeleteAccState.Success(Unit)
                    deleteTokenUseCase()
                }
            }
        }.launchIn(viewModelScope)
    }

}

sealed class DeleteAccState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : DeleteAccState(data)
    class Error(message: String, code: Int?) : DeleteAccState(message = message, code = code)
    class Loading : DeleteAccState()
}

sealed class LogoutState(val data: Unit? = null, val message: String? = null, val code: Int? = null){
    class Success(data: Unit) : LogoutState(data)
    class Error(message: String, code: Int?) : LogoutState(message = message, code = code)
    class Loading : LogoutState()
}

sealed class ResultCropState(val data: ImageBitmap? = null, val message: String? = null, code: Int? = null){
    class Success(data: ImageBitmap) : ResultCropState(data = data)
    class Error(message: String, code: Int?) : ResultCropState(message = message, code = code)
    class Loading : ResultCropState()
}

sealed class SendCropState(val data: Unit? = null, val message: String? = null, code: Int? = null){
    class Success(data: Unit) : SendCropState(data = data)
    class Error(message: String, code: Int?) : SendCropState(message = message, code = code)
    class Loading : SendCropState()
}

sealed class ProfileState(val data: SelfUserEntity? = null, val message: String? = null, code: Int? = null){
    class Success(data: SelfUserEntity) : ProfileState(data = data)
    class Error(message: String, code: Int?) : ProfileState(message = message, code = code)
    class Loading : ProfileState()
}
