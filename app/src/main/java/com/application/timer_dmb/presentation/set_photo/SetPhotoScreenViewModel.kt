package com.application.timer_dmb.presentation.set_photo

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.usecases.user.SavePhotoUseCase
import com.application.timer_dmb.presentation.profile.ResultCropState
import com.application.timer_dmb.presentation.profile.SendCropState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class SetPhotoScreenViewModel @Inject constructor(
    private val savePhotoUseCase: SavePhotoUseCase,
): ViewModel(){
    private val _cropState = MutableStateFlow<ResultCropState?>(null)
    val cropState = _cropState.asStateFlow()

    private val _sendCropState = MutableStateFlow<SendCropState?>(null)
    val sendCropState = _sendCropState.asStateFlow()

    fun setCropState(bitmap: ImageBitmap){
        _cropState.value = ResultCropState.Success(data = bitmap)
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
}