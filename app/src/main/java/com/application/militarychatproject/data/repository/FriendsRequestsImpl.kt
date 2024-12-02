package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.FriendsRequests
import com.application.militarychatproject.data.remote.dto.AcceptedFriendDTO
import com.application.militarychatproject.data.remote.dto.FriendDTO
import com.application.militarychatproject.data.remote.dto.ReceiverDTO
import com.application.militarychatproject.data.remote.dto.SenderDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.FriendsRepository
import javax.inject.Inject

class FriendsRequestsImpl @Inject constructor (
    private val requests: FriendsRequests
) : FriendsRepository {
    override suspend fun acceptFriend(senderId: String): ApiResponse<AcceptedFriendDTO> {
        return requests.acceptFriendRequest(senderId)
    }

    override suspend fun deleteFriend(friendId: String): ApiResponse<Any> {
        return requests.deleteFriendRequest(friendId)
    }

    override suspend fun getFriends(): ApiResponse<List<FriendDTO>> {
        return requests.getFriendsRequest()
    }

    override suspend fun getListOfReceivedRequests(): ApiResponse<List<SenderDTO>> {
        return requests.getListOfReceivedRequest()
    }

    override suspend fun getListOfSandedRequests(): ApiResponse<List<ReceiverDTO>> {
        return requests.getListOfSandedRequest()
    }

    override suspend fun sendFriendRequest(receiverId: String): ApiResponse<Any> {
        return requests.sendFriendRequest(receiverId)
    }
}