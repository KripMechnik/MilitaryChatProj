package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.FriendsRequests
import com.application.timer_dmb.data.remote.dto.AcceptedFriendDTO
import com.application.timer_dmb.data.remote.dto.FriendDTO
import com.application.timer_dmb.data.remote.dto.ReceiverDTO
import com.application.timer_dmb.data.remote.dto.SenderDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.repository.FriendsRepository
import javax.inject.Inject

class FriendsRepoImpl @Inject constructor (
    private val requests: FriendsRequests
) : FriendsRepository {
    override suspend fun acceptFriend(senderId: String): ApiResponse<AcceptedFriendDTO> {
        return requests.acceptFriendRequest(senderId)
    }

    override suspend fun deleteFriend(friendId: String): ApiResponse<Unit> {
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

    override suspend fun sendFriendRequest(receiverId: String): ApiResponse<Unit> {
        return requests.sendFriendRequest(receiverId)
    }
}