package com.application.timer_dmb.domain.usecases.authorization

import com.application.timer_dmb.common.UserData
import com.application.timer_dmb.data.remote.network.AuthRequest
import javax.inject.Inject

class LogoutWhenNoConnectionUseCase @Inject constructor(
    private val userData: UserData,
    private val authRequest: AuthRequest
) {
    operator fun invoke(){
        userData.deleteToken()
        authRequest.invalidateTokens()
    }
}