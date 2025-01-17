package com.application.timer_dmb.presentation.menu

import android.graphics.Bitmap
import kotlinx.coroutines.flow.StateFlow

interface MenuScreenPresenter {

    val state: StateFlow<MenuState?>

    val registered: StateFlow<Boolean>

    fun navigateToRegister()

    fun navigateToProfile()

    fun navigateToCalendar()

    fun checkAuthorized()

    fun getSelfUser()

    fun navigateToSettings()

    fun saveBitmapAsFile(bitmap: Bitmap)
}