package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.AcceptedFriendDTO
import com.application.timer_dmb.data.remote.dto.FriendDTO
import com.application.timer_dmb.data.remote.dto.ReceiverDTO
import com.application.timer_dmb.data.remote.dto.SenderDTO
import com.application.timer_dmb.data.remote.network.ApiResponse

interface FriendsRepository {

    suspend fun acceptFriend(senderId: String): ApiResponse<AcceptedFriendDTO>

    suspend fun deleteFriend(friendId: String): ApiResponse<Unit>

    suspend fun getFriends(): ApiResponse<List<FriendDTO>>

    suspend fun getListOfReceivedRequests(): ApiResponse<List<SenderDTO>>

    suspend fun getListOfSandedRequests(): ApiResponse<List<ReceiverDTO>>

    suspend fun sendFriendRequest(receiverId: String): ApiResponse<Unit>

}