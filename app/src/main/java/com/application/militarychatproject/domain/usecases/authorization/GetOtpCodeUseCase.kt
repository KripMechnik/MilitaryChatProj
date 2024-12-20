package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.GetOtpBodyEntity
import com.application.militarychatproject.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetOtpCodeUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) {
    operator fun invoke(otpBody: GetOtpBodyEntity) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = authorizationRepository.getOtpRequest(otpBody)
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }


}