package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.NewUserEntity
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.ResetPasswordEntity
import com.application.timer_dmb.domain.entity.send.SendOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.SendOtpResetBodyEntity
import com.application.timer_dmb.domain.entity.send.SignedInUserEntity

interface AuthorizationRepository {

    suspend fun registration(newUser: NewUserEntity) : ApiResponse<Unit>

    suspend fun signIn(signedInUser: SignedInUserEntity) : ApiResponse<TokenDTO>

    suspend fun logout() : ApiResponse<Unit>

    suspend fun deleteAccount() : ApiResponse<Unit>

    suspend fun getOtpRequest(otpBody: GetOtpBodyEntity) : ApiResponse<Unit>

    suspend fun sendOtpRequest(otpBody: SendOtpBodyEntity) : ApiResponse<TokenDTO>

    suspend fun getOtpForResetPasswordRequest(otpBody: GetOtpBodyEntity) : ApiResponse<Unit>

    suspend fun sendOtpForResetPasswordRequest(otpBody: SendOtpResetBodyEntity) : ApiResponse<Unit>

    suspend fun resetPasswordRequest(newPassword: ResetPasswordEntity) : ApiResponse<Unit>

}