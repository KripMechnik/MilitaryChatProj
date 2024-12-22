package com.application.militarychatproject.domain.usecases.user

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.toPhotoEntity
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.receive.PhotoEntity
import com.application.militarychatproject.domain.repository.UserRepository
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