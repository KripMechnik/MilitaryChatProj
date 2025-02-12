package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.TokenDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.AuthRequest
import com.application.timer_dmb.data.remote.network.BaseRequest
import com.application.timer_dmb.domain.entity.send.NewUserEntity
import com.application.timer_dmb.domain.entity.send.GetOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.ResetPasswordEntity
import com.application.timer_dmb.domain.entity.send.SendOtpBodyEntity
import com.application.timer_dmb.domain.entity.send.SendOtpResetBodyEntity
import com.application.timer_dmb.domain.entity.send.SignedInUserEntity
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.plugin
import io.ktor.http.HttpMethod
import javax.inject.Inject
import javax.inject.Named

class AuthorizationRequests @Inject constructor(
    private val baseRequest: BaseRequest,
    private val authRequest: AuthRequest
) {

    private val basePath = "/auth/"

    //token -> no
    suspend fun registrationRequest(
        newUser: NewUserEntity
    ) : ApiResponse<Unit> {
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
        authRequest.invalidateTokens()
        return baseRequest(
            method = HttpMethod.Post,
            body = signedInUser,
            path = basePath + "sign-in"
        )
    }

    //token -> yes
    suspend fun logoutRequest() : ApiResponse<Unit> {
        val result: ApiResponse<Unit> = authRequest(
            method = HttpMethod.Post,
            path = basePath + "log-out"
        )
        authRequest.invalidateTokens()
        return result
    }

    //token -> yes
    suspend fun deleteRequest() : ApiResponse<Unit>{
        val result: ApiResponse<Unit> = authRequest(
            method = HttpMethod.Delete,
            path = basePath
        )
        authRequest.invalidateTokens()
        return result
    }

    //token -> no
    suspend fun getOtpRequest(
        otpBody: GetOtpBodyEntity
    ) : ApiResponse<Unit> {
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "send-email-verification-otp",
            body = otpBody
        )
    }

    suspend fun sendOtpRequest(
        otpBody: SendOtpBodyEntity
    ) : ApiResponse<TokenDTO> {
        authRequest.invalidateTokens()
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "verify-email",
            body = otpBody
        )
    }
    suspend fun getOtpForResetPassword(
        otpBody: GetOtpBodyEntity
    ): ApiResponse<Unit>{
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "send-password-reset-otp",
            body = otpBody
        )
    }

    suspend fun sendOtpForResetPassword(
        otpBody: SendOtpResetBodyEntity
    ) : ApiResponse<Unit>{
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "verify-password-reset",
            body = otpBody
        )
    }

    suspend fun resetPassword(
        newPassword: ResetPasswordEntity
    ) : ApiResponse<Unit>{
        return baseRequest(
            method = HttpMethod.Post,
            path = basePath + "reset-password",
            body = newPassword
        )
    }

}