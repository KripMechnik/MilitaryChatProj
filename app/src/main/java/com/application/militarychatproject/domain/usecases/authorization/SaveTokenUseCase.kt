package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.UserData
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val userData: UserData
) {
    operator fun invoke(
        accessToken: String,
        refreshToken: String,
        accessTokenExpiresAt: Long,
        refreshTokenExpiresAt: Long
    ){
        userData.setToken(
            accessToken,
            refreshToken,
            accessTokenExpiresAt,
            refreshTokenExpiresAt
        )
    }
}