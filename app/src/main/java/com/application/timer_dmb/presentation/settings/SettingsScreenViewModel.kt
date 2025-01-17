package com.application.timer_dmb.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.domain.usecases.user.DeleteBackgroundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val deleteBackgroundUseCase: DeleteBackgroundUseCase
) : ViewModel() {

    fun resetBackground(){
        viewModelScope.launch{
            deleteBackgroundUseCase()
        }
    }

}