package com.application.timer_dmb.data.remote

import com.application.timer_dmb.data.remote.dto.AcceptedFriendDTO
import com.application.timer_dmb.data.remote.dto.FriendDTO
import com.application.timer_dmb.data.remote.dto.ReceiverDTO
import com.application.timer_dmb.data.remote.dto.SenderDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.data.remote.network.AuthRequest
import io.ktor.http.HttpMethod
import io.ktor.util.StringValues
import javax.inject.Inject

class FriendsRequests @Inject constructor(
    private val authRequest: AuthRequest
) {

    private val basePath = "/friendship/"

    //token -> yes
    suspend fun acceptFriendRequest(
        senderId: String
    ): ApiResponse<AcceptedFriendDTO>{
        return authRequest(
            method = HttpMethod.Post,
            params = StringValues.build {
                append("senderId", senderId)
            },
            path = basePath + "accept-request/"
        )
    }

    //token -> yes
    suspend fun deleteFriendRequest(
        friendId: String
    ): ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Delete,
            params = StringValues.build {
                append("friendId", friendId)
            },
            path = basePath
        )
    }

    //token -> yes
    suspend fun getFriendsRequest(): ApiResponse<List<FriendDTO>>{
        return authRequest(
            method = HttpMethod.Get,
            path = basePath + "friends"
        )
    }

    //token -> yes
    suspend fun getListOfReceivedRequest(): ApiResponse<List<SenderDTO>>{
        return authRequest(
            method = HttpMethod.Get,
            path = basePath + "received-requests"
        )
    }


    //token -> yes
    suspend fun getListOfSandedRequest(): ApiResponse<List<ReceiverDTO>>{
        return authRequest(
            method = HttpMethod.Get,
            path = basePath + "sent-requests"
        )
    }

    //token -> yes
    suspend fun sendFriendRequest(
        receiverId: String
    ): ApiResponse<Unit>{
        return authRequest(
            method = HttpMethod.Post,
            params = StringValues.build {
                append("receiverId", receiverId)
            },
            path = basePath + "send-request"
        )
    }
}