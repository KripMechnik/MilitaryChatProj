package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewUserEntity
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity

interface AuthorizationRepository {

    suspend fun registration(newUser: NewUserEntity) : ApiResponse<TokenDTO>

    suspend fun signIn(signedInUser: SignedInUserEntity) : ApiResponse<TokenDTO>

    suspend fun logout() : ApiResponse<Unit>

    suspend fun deleteAccount() : ApiResponse<Unit>

}