package com.application.timer_dmb.presentation.profile

import androidx.compose.ui.graphics.ImageBitmap
import androidx.navigation.NavController
import kotlinx.coroutines.flow.StateFlow

class ProfileScreenPresenterImpl(
    private val viewModel: ProfileScreenViewModel,
    private val navController: NavController
) : ProfileScreenPresenter {

    override val cropState: StateFlow<ResultCropState?>
        get() = viewModel.cropState

    override val state: StateFlow<LogoutState?>
        get() = viewModel.state

    override val profileState: StateFlow<ProfileState?>
        get() = viewModel.profileState

    override val sendCropState: StateFlow<SendCropState?>
        get() = viewModel.sendCropState

    override val deleteAccState: StateFlow<DeleteAccState?>
        get() = viewModel.deleteAccState

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun logout() {
        viewModel.logout()
    }

    override fun navigateToMenu() {
        navController.navigateUp()
    }

    override fun setCropState(bitmap: ImageBitmap) {
        viewModel.setCropState(bitmap)
    }

    override fun sendPhoto() {
        viewModel.sendImage()
    }

    override fun getPhoto() {
        viewModel.getPhoto()
    }

    override fun delete() {
        viewModel.delete()
    }
}