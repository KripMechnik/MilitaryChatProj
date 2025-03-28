package com.application.timer_dmb.domain.usecases.user

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toPhotoEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.PhotoEntity
import com.application.timer_dmb.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPhotoUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    operator fun invoke() : Flow<Resource<PhotoEntity>> = flow {
        emit(Resource.Loading())
        val response = userRepository.getPhoto()
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toPhotoEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}