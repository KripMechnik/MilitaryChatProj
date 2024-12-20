package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.UserData
import javax.inject.Inject

class DeleteTokenUseCase @Inject constructor(
    private val userData: UserData
) {
    operator fun invoke(){
        userData.deleteToken()
    }
}