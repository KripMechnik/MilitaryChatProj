package com.application.timer_dmb.data.repository

import com.application.timer_dmb.data.remote.UserRequests
import com.application.timer_dmb.data.remote.dto.PhotoDTO
import com.application.timer_dmb.data.remote.dto.SelfUserDTO
import com.application.timer_dmb.data.remote.dto.UserDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UserTypeEntity
import com.application.timer_dmb.domain.repository.UserRepository
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

    override suspend fun savePhoto(byteArray: ByteArray): ApiResponse<Unit> {
        return userRequests.savePhotoRequest(byteArray)
    }

    override suspend fun setStatusOnline(): ApiResponse<Unit> {
        return userRequests.setStatusOnlineRequest()
    }

    override suspend fun setStatusOffline(): ApiResponse<Unit> {
        return userRequests.setStatusOfflineRequest()
    }

    override suspend fun setUserType(userType: UserTypeEntity): ApiResponse<Unit> {
        return userRequests.setUserType(userType)
    }
}