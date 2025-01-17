package com.application.timer_dmb.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.timer_dmb.common.Constants.ADD_SOLDIER_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.HOME_SCREEN_ROUTE
import com.application.timer_dmb.domain.usecases.authorization.IsAddedSoldierUseCase
import com.application.timer_dmb.domain.usecases.authorization.IsAuthorizedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val isAuthorizedUseCase: IsAuthorizedUseCase,
    private val isAddedSoldierUseCase: IsAddedSoldierUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<SplashScreenState>(SplashScreenState.Idle)
    val state: StateFlow<SplashScreenState> get() = _state

    private val _route = MutableStateFlow("")
    val route: StateFlow<String> get() = _route

    init {
        viewModelScope.launch {
            if (isAuthorizedUseCase() || isAddedSoldierUseCase()) {
                _route.value = HOME_SCREEN_ROUTE
                _state.value = SplashScreenState.Authorized
                Log.i("auth", "yes")
            } else {
                _route.value = ADD_SOLDIER_SCREEN_ROUTE
                _state.value = SplashScreenState.Unauthorized
                Log.i("auth", "no")
            }
        }
    }

}

sealed class SplashScreenState() {
    data object Idle: SplashScreenState()
    data object Authorized: SplashScreenState()
    data object Unauthorized: SplashScreenState()
}