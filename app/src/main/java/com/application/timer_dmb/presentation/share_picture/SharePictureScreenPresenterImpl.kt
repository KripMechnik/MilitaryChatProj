package com.application.timer_dmb.presentation.share_picture

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.navigation.NavController
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.messanger.all_chats.ChatsState
import com.application.timer_dmb.presentation.messanger.chat.SingleMessageState
import com.application.timer_dmb.presentation.profile.ProfileState
import kotlinx.coroutines.flow.StateFlow

class SharePictureScreenPresenterImpl(
    private val viewModel: SharePictureScreenViewModel,
    private val navController: NavController
) : SharePictureScreenPresenter {

    override val state: StateFlow<HomeState>
        get() = viewModel.state

    override val profileState: StateFlow<ProfileState?>
        get() = viewModel.profileState

    override val shareImageBitmap: StateFlow<ImageBitmap?>
        get() = viewModel.shareImageBitmap

    override val messageState: StateFlow<SingleMessageState?>
        get() = viewModel.singleMessageState

    override val chatState: StateFlow<ChatsState?>
        get() = viewModel.chatsState

    override val backgroundState: StateFlow<BackgroundState?>
        get() = viewModel.background

    override fun isAuthorized(): Boolean {
        return viewModel.isAuthorized()
    }

    override fun getImage(bitmap: ImageBitmap): Uri? {
        return viewModel.getImage(bitmap.asAndroidBitmap())
    }

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun sendBitmap(bitmap: ImageBitmap) {
        viewModel.sendBitmap(bitmap)
    }

    override fun sendTimer() {
        viewModel.sendTimer()
    }
}