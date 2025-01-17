package com.application.timer_dmb.presentation.set_photo

import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.TYPE_SCREEN_ROUTE
import com.application.timer_dmb.presentation.profile.ResultCropState
import com.application.timer_dmb.presentation.profile.SendCropState
import kotlinx.coroutines.flow.StateFlow

class SetPhotoScreenPresenterImpl(
    private val viewModel: SetPhotoScreenViewModel,
    private val navController: NavController
): SetPhotoScreenPresenter {

    override val sendCropState: StateFlow<SendCropState?>
        get() = viewModel.sendCropState

    override val cropState: StateFlow<ResultCropState?>
        get() = viewModel.cropState

    override fun setCropState(bitmap: ImageBitmap) {
        viewModel.setCropState(bitmap)
    }

    override fun sendPhoto() {
        viewModel.sendImage()
    }

    override fun navigateToChooseType() {
        navController.navigate(TYPE_SCREEN_ROUTE)
    }


}