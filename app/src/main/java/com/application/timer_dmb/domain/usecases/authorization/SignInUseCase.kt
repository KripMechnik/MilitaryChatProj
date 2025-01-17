package com.application.timer_dmb.domain.usecases.authorization

import com.application.timer_dmb.common.Resource
import com.application.timer_dmb.data.remote.dto.toTokenEntity
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.receive.TokenEntity
import com.application.timer_dmb.domain.entity.send.SignedInUserEntity
import com.application.timer_dmb.domain.repository.AuthorizationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authorizationRepository: AuthorizationRepository
) {
    operator fun invoke(signedInUser: SignedInUserEntity) : Flow<Resource<TokenEntity>> = flow {
        emit(Resource.Loading())
        val response = authorizationRepository.signIn(signedInUser)
        if (response is ApiResponse.Success) emit(Resource.Success(data = response.data!!.toTokenEntity()))
        else emit(Resource.Error(message = response.errorMessage ?: "Unknown error", code = response.errorCode))
    }
}