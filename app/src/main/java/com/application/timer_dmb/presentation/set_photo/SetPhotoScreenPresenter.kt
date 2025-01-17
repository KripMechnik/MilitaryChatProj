package com.application.timer_dmb.presentation.set_photo

import androidx.compose.ui.graphics.ImageBitmap
import com.application.timer_dmb.presentation.profile.ResultCropState
import com.application.timer_dmb.presentation.profile.SendCropState
import kotlinx.coroutines.flow.StateFlow

interface SetPhotoScreenPresenter {

    val sendCropState: StateFlow<SendCropState?>

    val cropState: StateFlow<ResultCropState?>

    fun setCropState(bitmap: ImageBitmap)

    fun sendPhoto()

    fun navigateToChooseType()
}