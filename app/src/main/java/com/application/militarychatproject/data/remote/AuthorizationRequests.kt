package com.application.militarychatproject.data.remote

import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
import com.application.militarychatproject.domain.entity.send.NewUserEntity
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity
import io.ktor.http.HttpMethod
import javax.inject.Inject

class AuthorizationRequests @Inject constructor(
    private val baseRequest: BaseRequest,
    private val authRequest: AuthRequest
) {

    private val basePath = "/auth/"

    //token -> no
    suspend fun registrationRequest(
        newUser: NewUserEntity
    ) : ApiResponse<TokenDTO> {
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "sign-up",
            body = newUser
        )
    }

    //token -> no
    suspend fun signInRequest(
        signedInUser: SignedInUserEntity
    ) : ApiResponse<TokenDTO> {
        return baseRequest(
            method = HttpMethod.Post,
            body = signedInUser,
            path = basePath + "sign-in"
        )
    }

    //token -> yes
    suspend fun logoutRequest() : ApiResponse<Unit> {
        return authRequest(
            method = HttpMethod.Post,
            path = basePath + "log-out"
        )
    }

    //token -> yes
    suspend fun deleteRequest() : ApiResponse<Unit>{
        return baseRequest(
            method = HttpMethod.Delete,
            path = basePath
        )
    }

}