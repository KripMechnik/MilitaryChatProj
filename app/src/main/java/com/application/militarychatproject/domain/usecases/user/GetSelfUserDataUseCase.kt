package com.application.militarychatproject.domain.usecases.user

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toSelfUserEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import com.application.militarychatproject.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSelfUserDataUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() : Flow<Resource<SelfUserEntity>> = flow {
        emit(Resource.Loading())
        val response = userRepository.getSelfUser()
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toSelfUserEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}