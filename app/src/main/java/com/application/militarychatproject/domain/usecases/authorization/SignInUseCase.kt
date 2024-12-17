package com.application.militarychatproject.domain.usecases.authorization

import com.application.militarychatproject.common.Resource
import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewUserEntity
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity
import com.application.militarychatproject.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) {
    operator fun invoke(signedInUser: SignedInUserEntity) : Flow<Resource<TokenDTO>> = flow {
        emit(Resource.Loading())
        val response = authorizationRepository.signIn(signedInUser)
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}