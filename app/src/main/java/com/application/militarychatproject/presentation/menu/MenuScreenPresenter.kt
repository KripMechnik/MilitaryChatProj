package com.application.militarychatproject.presentation.menu

import kotlinx.coroutines.flow.StateFlow

interface MenuScreenPresenter {

    val state: StateFlow<MenuState?>

    val registered: StateFlow<Boolean>

    fun navigateToRegister()

    fun navigateToProfile()

    fun checkAuthorized()
}