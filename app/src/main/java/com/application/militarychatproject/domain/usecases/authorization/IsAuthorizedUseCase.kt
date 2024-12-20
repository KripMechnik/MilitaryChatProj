package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.UserData
import com.application.militarychatproject.domain.repository.RefreshTokenRepository
import javax.inject.Inject

class IsAuthorizedUseCase @Inject constructor(
    private val userData: UserData
) {
    operator fun invoke() : Boolean {
        return userData.checkAuthorized()
    }
}