package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.PhotoDTO
import com.application.timer_dmb.data.remote.dto.SelfUserDTO
import com.application.timer_dmb.data.remote.dto.UserDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.AuthRequest
import com.application.timer_dmb.data.remote.network.BaseRequest
import com.application.timer_dmb.domain.entity.send.UserTypeEntity
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class UserRequests @Inject constructor(
    private val baseRequest: BaseRequest,
    private val authRequest: AuthRequest
) {

    private val basePath = "/user/"

    //token -> yes
    suspend fun getSelfUserRequest() : ApiResponse<SelfUserDTO> {
        return authRequest(
            method = HttpMethod.Get,
            path = basePath
        )
    }

    //token -> yes
    suspend fun deletePhotoRequest() : ApiResponse<Unit> {
        return authRequest(
            method = HttpMethod.Delete,
            path = basePath + "avatar"
        )
    }

    //token -> yes
    suspend fun getPhotoRequest() : ApiResponse<PhotoDTO>{
        return authRequest(
            method = HttpMethod.Get,
            path = basePath + "avatar"
        )
    }

    //token -> yes
    suspend fun banUser(
        userId: String
    ) : ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Put,
            query = userId,
            path = basePath + "ban-user/"
        )
    }

    //token -> yes
    suspend fun setUserType(
        userType: UserTypeEntity
    ) : ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath + "user-type",
            body = userType
        )
    }

    //token -> no
    suspend fun getUserByIdRequest(
        userId: String
    ) : ApiResponse<UserDTO>{
        return baseRequest(
            method = HttpMethod.Get,
            params = StringValues.build {
                append("userId", userId)
            },
            path = basePath + "id/"
        )
    }

    //token -> yes
    suspend fun getUserByNameRequest(
        username: String
    ) : ApiResponse<UserDTO>{
        return authRequest(
            method = HttpMethod.Get,
            params = StringValues.build {
                append("userName", username)
            },
            path = basePath + "name/"
        )
    }

    //token -> yes
    suspend fun savePhotoRequest(byteArray: ByteArray) : ApiResponse<Unit>{
        return authRequest(
            formData = true,
            path = basePath + "avatar",
            method = HttpMethod.Post,
            body = MultiPartFormDataContent(
                formData {
                    append("image", byteArray, Headers.build {
                        append(HttpHeaders.ContentType, "image/png")
                        append(HttpHeaders.ContentDisposition, "filename=\"avatar\"")
                    })

                }
            )
        )
    }

    //token -> yes
    suspend fun setStatusOfflineRequest() : ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath + "set-status-offline"
        )
    }

    //token -> yes
    suspend fun setStatusOnlineRequest(): ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath + "set-status-online"
        )
    }
}