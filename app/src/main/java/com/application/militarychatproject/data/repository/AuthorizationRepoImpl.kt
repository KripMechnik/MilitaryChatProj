package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.AuthorizationRequests
import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewUserEntity
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity
import com.application.militarychatproject.domain.repository.AuthorizationRepository
import javax.inject.Inject

class AuthorizationRepoImpl @Inject constructor(
    private val requests: AuthorizationRequests
) : AuthorizationRepository {
    override suspend fun registration(newUser: NewUserEntity): ApiResponse<TokenDTO> {
        return requests.registrationRequest(newUser)
    }

    override suspend fun signIn(signedInUser: SignedInUserEntity): ApiResponse<TokenDTO> {
        return requests.signInRequest(signedInUser)
    }

    override suspend fun logout(): ApiResponse<Unit> {
        return requests.logoutRequest()
    }

    override suspend fun deleteAccount(): ApiResponse<Unit> {
        return requests.deleteRequest()
    }

}