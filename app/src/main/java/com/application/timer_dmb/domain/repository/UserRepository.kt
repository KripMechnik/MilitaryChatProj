package com.application.timer_dmb.domain.repository

import com.application.timer_dmb.data.remote.dto.PhotoDTO
import com.application.timer_dmb.data.remote.dto.SelfUserDTO
import com.application.timer_dmb.data.remote.dto.UserDTO
import com.application.timer_dmb.data.remote.network.ApiResponse
import com.application.timer_dmb.domain.entity.send.UserTypeEntity

interface UserRepository {

    suspend fun deletePhoto(): ApiResponse<Unit>

    suspend fun banUser(userId: String): ApiResponse<Unit>

    suspend fun getPhoto(): ApiResponse<PhotoDTO>

    suspend fun getSelfUser(): ApiResponse<SelfUserDTO>

    suspend fun getUserByName(username: String): ApiResponse<UserDTO>

    suspend fun getUserById(id: String): ApiResponse<UserDTO>

    suspend fun savePhoto(byteArray: ByteArray): ApiResponse<Unit>

    suspend fun setStatusOnline(): ApiResponse<Unit>

    suspend fun setStatusOffline(): ApiResponse<Unit>

    suspend fun setUserType(userType: UserTypeEntity): ApiResponse<Unit>

}