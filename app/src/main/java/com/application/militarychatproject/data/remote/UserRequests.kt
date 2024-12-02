package com.application.militarychatproject.data.remote

import com.application.militarychatproject.data.remote.dto.PhotoDTO
import com.application.militarychatproject.data.remote.dto.SelfUserDTO
import com.application.militarychatproject.data.remote.dto.UserDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
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
    suspend fun deletePhotoRequest() : ApiResponse<Any> {
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
    suspend fun savePhotoRequest(){}

    //token -> yes
    suspend fun setStatusOfflineRequest() : ApiResponse<Any>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath + "set-status-offline"
        )
    }

    //token -> yes
    suspend fun setStatusOnlineRequest(): ApiResponse<Any>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath + "set-status-online"
        )
    }
}