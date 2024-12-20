package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.TokenDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.entity.send.NewUserEntity
import com.application.militarychatproject.domain.entity.send.GetOtpBodyEntity
import com.application.militarychatproject.domain.entity.send.SendOtpBodyEntity
import com.application.militarychatproject.domain.entity.send.SignedInUserEntity

interface AuthorizationRepository {

    suspend fun registration(newUser: NewUserEntity) : ApiResponse<Unit>

    suspend fun signIn(signedInUser: SignedInUserEntity) : ApiResponse<TokenDTO>

    suspend fun logout() : ApiResponse<Unit>

    suspend fun deleteAccount() : ApiResponse<Unit>

    suspend fun getOtpRequest(otpBody: GetOtpBodyEntity) : ApiResponse<Unit>

    suspend fun sendOtpRequest(otpBody: SendOtpBodyEntity) : ApiResponse<TokenDTO>

}