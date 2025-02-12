package com.application.timer_dmb.presentation.menu

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.domain.entity.receive.SelfUserEntity
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import com.application.timer_dmb.domain.usecases.authorization.LogoutWhenNoConnectionUseCase
import com.application.timer_dmb.domain.usecases.user.GetSelfUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class MenuScreenViewModel @Inject constructor(
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val getSelfUserDataUseCase: GetSelfUserDataUseCase,
    private val logoutWhenNoConnectionUseCase: LogoutWhenNoConnectionUseCase,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val _registered = MutableStateFlow(false)
    val registered = _registered.asStateFlow()

    private val _state = MutableStateFlow<MenuState?>(null)
    val state = _state.asStateFlow()

    fun checkAuthorized() {
        if (_registered.value != isAuthorizedUseCase()) _registered.value = isAuthorizedUseCase()
    }
    fun getSelfUser(){
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

    fun logoutWhenNoConnection(){
        logoutWhenNoConnectionUseCase()
    }

    fun saveBitmapAsFile(bitmap: Bitmap){

        viewModelScope.launch {
            withContext(NonCancellable + Dispatchers.IO){
                val file = File(context.cacheDir, "background_image.png")
                file.createNewFile()

                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)

                val bitmapData = bos.toByteArray()

                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()
            }
        }
    }
}

sealed class MenuState(val data: SelfUserEntity? = null, val message: String? = null, val code: Int? = null){
    class Success(data: SelfUserEntity) : MenuState(data = data)
    class Error(message: String, code: Int?) : MenuState(message = message, code = code)
    class Loading : MenuState()
}