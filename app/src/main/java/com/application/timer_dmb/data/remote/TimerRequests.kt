package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.TimerDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.AuthRequest
import com.application.timer_dmb.domain.entity.send.UpdatedTimerEntity
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
    ) : ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Put,
            path = basePath,
            body = updatedTimer
        )
    }

}