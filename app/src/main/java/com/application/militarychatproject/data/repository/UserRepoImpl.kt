package com.application.militarychatproject.data.repository

import com.application.militarychatproject.data.remote.UserRequests
import com.application.militarychatproject.data.remote.dto.PhotoDTO
import com.application.militarychatproject.data.remote.dto.SelfUserDTO
import com.application.militarychatproject.data.remote.dto.UserDTO
import com.application.militarychatproject.data.remote.network.ApiResponse
import com.application.militarychatproject.domain.repository.UserRepository
import javax.inject.Inject

class UserRepoImpl @Inject constructor (
    private val userRequests: UserRequests
) : UserRepository {

    override suspend fun deletePhoto(): ApiResponse<Unit> {
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

    override suspend fun savePhoto(): ApiResponse<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun setStatusOnline(): ApiResponse<Unit> {
        return userRequests.setStatusOnlineRequest()
    }

    override suspend fun setStatusOffline(): ApiResponse<Unit> {
        return userRequests.setStatusOfflineRequest()
    }
}