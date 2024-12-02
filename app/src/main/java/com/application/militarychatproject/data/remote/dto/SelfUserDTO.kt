package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import kotlinx.serialization.Serializable

@Serializable
data class SelfUserDTO(
    val avatarImageName: String?,
    val id: String,
    val login: String,
    val name: String,
    val nickname: String,
    val online: String,
    val userType: String
)

fun SelfUserDTO.toSelfUserEntity() = SelfUserEntity(
    avatarImageName = this.avatarImageName,
    id = this.id,
    login = this.login,
    name = this.name,
    nickname = this.nickname,
    online = this.online,
    userType = this.userType
)