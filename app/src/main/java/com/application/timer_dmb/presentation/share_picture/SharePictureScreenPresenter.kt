package com.application.timer_dmb.presentation.share_picture

import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.application.timer_dmb.presentation.home.BackgroundState
import com.application.timer_dmb.presentation.home.HomeState
import com.application.timer_dmb.presentation.messanger.all_chats.ChatsState
import com.application.timer_dmb.presentation.messanger.chat.SingleMessageState
import com.application.timer_dmb.presentation.profile.ProfileState
import kotlinx.coroutines.flow.StateFlow

interface SharePictureScreenPresenter {

    val state: StateFlow<HomeState>

    val profileState: StateFlow<ProfileState?>

    val shareImageBitmap: StateFlow<ImageBitmap?>

    val messageState: StateFlow<SingleMessageState?>

    val chatState: StateFlow<ChatsState?>

    val backgroundState: StateFlow<BackgroundState?>

    fun isAuthorized(): Boolean

    fun getImage(bitmap: ImageBitmap): Uri?

    fun navigateUp()

    fun sendBitmap(bitmap: ImageBitmap)

    fun sendTimer()
}