package com.application.militarychatproject.data.remote

import com.application.militarychatproject.data.remote.dto.TimerDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.data.remote.network.AuthRequest
import com.application.militarychatproject.data.remote.network.BaseRequest
import com.application.militarychatproject.domain.entity.send.UpdatedTimerEntity
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class TimerRequests @Inject constructor(
    private val authRequest: AuthRequest
) {

    private val basePath = "/timer/"

    //token -> yes
    suspend fun getTimerDataRequest() : ApiResponse<TimerDTO>{
        return authRequest(
            method = HttpMethod.Get,
            path = basePath
        )
    }

    //token -> yes
    suspend fun synchronizeTimerRequest(
        timerId: String
    ) : ApiResponse<TimerDTO>{
        return authRequest(
            method = HttpMethod.Post,
            path = basePath + "connect/",
            params = StringValues.build {
                append("timerId", timerId)
            }
        )
    }

    //token -> yes
    suspend fun updateTimerRequest(
        updatedTimer: UpdatedTimerEntity
    ) : ApiResponse<TimerDTO>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath,
            body = updatedTimer
        )
    }

}