package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.UserEntity
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    val avatarImageName: String?,
    val id: Int,
    val name: String,
    val nickname: String,
)

fun UserDTO.toUserEntity() = UserEntity(
    avatarImageName = this.avatarImageName,
    id = this.id,
    name = this.name,
    nickname = this.nickname
)