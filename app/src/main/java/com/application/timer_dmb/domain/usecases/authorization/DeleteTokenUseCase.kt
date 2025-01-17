package com.application.timer_dmb.domain.usecases.authorization

import com.application.timer_dmb.common.UserData
import javax.inject.Inject

class DeleteTokenUseCase @Inject constructor(
    private val userData: UserData
) {
    operator fun invoke(){
        userData.deleteToken()
    }
}