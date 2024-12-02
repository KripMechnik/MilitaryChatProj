package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.AcceptedFriendDTO
import com.application.militarychatproject.data.remote.dto.FriendDTO
import com.application.militarychatproject.data.remote.dto.ReceiverDTO
import com.application.militarychatproject.data.remote.dto.SenderDTO
import com.application.militarychatproject.data.remote.network.ApiResponse

interface FriendsRepository {

    suspend fun acceptFriend(senderId: String): ApiResponse<AcceptedFriendDTO>

    suspend fun deleteFriend(friendId: String): ApiResponse<Any>

    suspend fun getFriends(): ApiResponse<List<FriendDTO>>

    suspend fun getListOfReceivedRequests(): ApiResponse<List<SenderDTO>>

    suspend fun getListOfSandedRequests(): ApiResponse<List<ReceiverDTO>>

    suspend fun sendFriendRequest(receiverId: String): ApiResponse<Any>

}