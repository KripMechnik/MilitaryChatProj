package com.application.timer_dmb.domain.usecases.user

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UserTypeEntity
import com.application.timer_dmb.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SetUserTypeUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userType: UserTypeEntity) : Flow<Resource<Unit>> = flow {
        emit(Resource.Loading())
        val response = userRepository.setUserType(userType)
        if (response is ApiResponse.Success) emit(Resource.Success(data = Unit))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}