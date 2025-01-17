package com.application.timer_dmb.presentation.reset_password.confirm_password

import androidx.navigation.NavController
import com.application.timer_dmb.common.Constants.LOGIN_SCREEN_ROUTE
import com.application.timer_dmb.common.Constants.RESET_PASSWORD_SCREEN_ROUTE
import kotlinx.coroutines.flow.StateFlow

class ConfirmPasswordPresenterImpl (
    private val viewModel: ConfirmPasswordViewModel,
    private val navController: NavController
): ConfirmPasswordPresenter {

    override val resetPasswordState: StateFlow<ResetPasswordState?>
        get() = viewModel.resetPasswordState

    override fun resetPassword(newPassword: String) {
        viewModel.resetPassword(newPassword)
    }

    override fun navigateUp() {
        navController.popBackStack()
    }

    override fun navigateToHome() {
        navController.navigate(LOGIN_SCREEN_ROUTE){
            popUpTo(RESET_PASSWORD_SCREEN_ROUTE){
                inclusive = true
            }
        }
    }
}