package com.application.timer_dmb.domain.usecases.authorization

import com.application.timer_dmb.common.UserData
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val userData: UserData
) {
    operator fun invoke(
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresAt: Long,
        refreshTokenExpiresAt: Long
    ): Boolean{
        return userData.setToken(
            accessToken,
            refreshToken,
            accessTokenExpiresAt,
            refreshTokenExpiresAt
        )
    }
}