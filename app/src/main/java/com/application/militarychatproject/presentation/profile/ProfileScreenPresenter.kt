package com.application.militarychatproject.presentation.profile

import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.StateFlow

interface ProfileScreenPresenter {

    val cropState: StateFlow<ResultCropState?>

    val state: StateFlow<LogoutState?>

    val profileState: StateFlow<ProfileState?>

    fun logout()

    fun navigateToMenu()

    fun setCropState(bitmap: ImageBitmap)
}