package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val avatarImageName: String?,
    val id: Int,
    val name: String,
    val nickname: String,
    val userType: String,
    val isAdmin: Boolean
)

fun UserDTO.toUserEntity() = UserEntity(
    avatarImageName = this.avatarImageName,
    id = this.id,
    name = this.name,
    nickname = this.nickname,
    isAdmin = this.isAdmin,
    userType = this.userType
)