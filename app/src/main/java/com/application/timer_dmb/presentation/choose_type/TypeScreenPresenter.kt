package com.application.timer_dmb.presentation.choose_type

import kotlinx.coroutines.flow.StateFlow

interface TypeScreenPresenter {

    val setTypeState: StateFlow<SetTypeState?>

    fun setType(type: String)

    fun navigateToHome()
}