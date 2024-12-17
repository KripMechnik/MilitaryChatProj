package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.UserData
import com.application.militarychatproject.domain.repository.RefreshTokenRepository
import javax.inject.Inject

class IsAuthorizedUseCase @Inject constructor(
    private val userData: UserData,
    private val refreshTokenRepository: RefreshTokenRepository
) {
    suspend operator fun invoke() : Boolean {
        if (userData.checkAuthorized()) {
            val response = refreshTokenRepository.refreshToken(userData.token.value.refreshToken)
            response.data?.let {
                apply {
                    userData.setToken(it.accessToken, it.refreshToken, it.accessTokenExpiresAt, it.refreshTokenExpiresAt)
                }
            }
            return true
        } else {
            return false
        }
    }
}