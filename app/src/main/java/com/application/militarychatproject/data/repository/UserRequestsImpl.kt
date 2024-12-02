package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.UserRequests
import com.application.militarychatproject.data.remote.dto.PhotoDTO
import com.application.militarychatproject.data.remote.dto.SelfUserDTO
import com.application.militarychatproject.data.remote.dto.UserDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.UserRepository
import javax.inject.Inject

class UserRequestsImpl @Inject constructor (
    private val userRequests: UserRequests
) : UserRepository {

    override suspend fun deletePhoto(): ApiResponse<Any> {
        return userRequests.deletePhotoRequest()
    }

    override suspend fun getPhoto(): ApiResponse<PhotoDTO> {
        return userRequests.getPhotoRequest()
    }

    override suspend fun getSelfUser(): ApiResponse<SelfUserDTO> {
        return userRequests.getSelfUserRequest()
    }

    override suspend fun getUserByName(username: String): ApiResponse<UserDTO> {
        return userRequests.getUserByNameRequest(username)
    }

    override suspend fun getUserById(id: String): ApiResponse<UserDTO> {
        return userRequests.getUserByIdRequest(id)
    }

    override suspend fun savePhoto(): ApiResponse<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun setStatusOnline(): ApiResponse<Any> {
        return userRequests.setStatusOnlineRequest()
    }

    override suspend fun setStatusOffline(): ApiResponse<Any> {
        return userRequests.setStatusOfflineRequest()
    }
}