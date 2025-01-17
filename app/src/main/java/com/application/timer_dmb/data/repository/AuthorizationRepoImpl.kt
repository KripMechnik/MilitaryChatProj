package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.AuthorizationRequests
import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.NewUserEntity
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.ResetPasswordEntity
import com.application.timer_dmb.domain.entity.send.SendOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.SendOtpResetBodyEntity
import com.application.timer_dmb.domain.entity.send.SignedInUserEntity
import com.application.timer_dmb.domain.repository.AuthorizationRepository
import javax.inject.Inject

class AuthorizationRepoImpl @Inject constructor(
    private val requests: AuthorizationRequests
) : AuthorizationRepository {
    override suspend fun registration(newUser: NewUserEntity): ApiResponse<Unit> {
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

    override suspend fun getOtpRequest(otpBody: GetOtpBodyEntity): ApiResponse<Unit> {
        return requests.getOtpRequest(otpBody)
    }

    override suspend fun sendOtpRequest(otpBody: SendOtpBodyEntity): ApiResponse<TokenDTO> {
        return requests.sendOtpRequest(otpBody)
    }

    override suspend fun getOtpForResetPasswordRequest(otpBody: GetOtpBodyEntity): ApiResponse<Unit> {
        return requests.getOtpForResetPassword(otpBody)
    }

    override suspend fun sendOtpForResetPasswordRequest(otpBody: SendOtpResetBodyEntity): ApiResponse<Unit> {
        return requests.sendOtpForResetPassword(otpBody)
    }

    override suspend fun resetPasswordRequest(newPassword: ResetPasswordEntity): ApiResponse<Unit> {
        return requests.resetPassword(newPassword)
    }


}