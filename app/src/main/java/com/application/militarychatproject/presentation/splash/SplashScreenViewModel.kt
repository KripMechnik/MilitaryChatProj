package com.application.militarychatproject.presentation.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.militarychatproject.common.UserData
import com.application.militarychatproject.presentation.profile.view.PROFILE_SCREEN_ROUTE
import com.application.militarychatproject.presentation.registration.first.view.REGISTRATION_SCREEN_ROUTE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val userData: UserData
) : ViewModel() {

    private val _state = MutableStateFlow<SplashScreenState>(SplashScreenState.Idle)
    val state: StateFlow<SplashScreenState> get() = _state

    private val _route = MutableStateFlow("")
    val route: StateFlow<String> get() = _route

    init {
        viewModelScope.launch {
            if (userData.checkAuthorized()) {
                _state.value = SplashScreenState.Authorized
                _route.value = PROFILE_SCREEN_ROUTE
                Log.i("auth", "yes")
            } else {
                _state.value = SplashScreenState.Unauthorized
                _route.value = REGISTRATION_SCREEN_ROUTE
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