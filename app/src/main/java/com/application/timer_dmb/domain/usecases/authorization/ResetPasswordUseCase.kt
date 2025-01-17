package com.application.timer_dmb.domain.usecases.authorization

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.ResetPasswordEntity
import com.application.timer_dmb.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) {
    operator fun invoke(otpBodyEntity: ResetPasswordEntity): Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = authorizationRepository.resetPasswordRequest(otpBodyEntity)
        if (response is ApiResponse.Success)
            emit(Resource.Success(Unit))
        else
            emit(
                Resource.Error(
                message = response.errorMessage ?: "Unknown error",
                code = response.errorCode
            ))
    }
}