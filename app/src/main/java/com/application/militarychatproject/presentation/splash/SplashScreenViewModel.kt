package com.application.militarychatproject.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.militarychatproject.common.Constants.HOME_SCREEN_ROUTE
import com.application.militarychatproject.domain.usecases.authorization.IsAuthorizedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val isAuthorizedUseCase: IsAuthorizedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SplashScreenState>(SplashScreenState.Idle)
    val state: StateFlow<SplashScreenState> get() = _state

    private val _route = MutableStateFlow("")
    val route: StateFlow<String> get() = _route

    init {
        viewModelScope.launch {
            if (isAuthorizedUseCase()) {
                _state.value = SplashScreenState.Authorized
                _route.value = HOME_SCREEN_ROUTE
                Log.i("auth", "yes")
            } else {
                _state.value = SplashScreenState.UnauthorizedNotAdded
                _route.value = HOME_SCREEN_ROUTE
                Log.i("auth", "no")
            }
        }
    }

}

sealed class SplashScreenState() {
    data object Idle: SplashScreenState()
    data object Authorized: SplashScreenState()
    data object UnauthorizedAdded: SplashScreenState()
    data object UnauthorizedNotAdded: SplashScreenState()
}