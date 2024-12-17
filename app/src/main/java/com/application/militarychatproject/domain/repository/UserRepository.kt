package com.application.militarychatproject.domain.repository

import com.application.militarychatproject.data.remote.dto.PhotoDTO
import com.application.militarychatproject.data.remote.dto.SelfUserDTO
import com.application.militarychatproject.data.remote.dto.UserDTO
import com.application.militarychatproject.data.remote.network.ApiResponse

interface UserRepository {

    suspend fun deletePhoto(): ApiResponse<Unit>

    suspend fun getPhoto(): ApiResponse<PhotoDTO>

    suspend fun getSelfUser(): ApiResponse<SelfUserDTO>

    suspend fun getUserByName(username: String): ApiResponse<UserDTO>

    suspend fun getUserById(id: String): ApiResponse<UserDTO>

    suspend fun savePhoto(): ApiResponse<Unit>

    suspend fun setStatusOnline(): ApiResponse<Unit>

    suspend fun setStatusOffline(): ApiResponse<Unit>

}