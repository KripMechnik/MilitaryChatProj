package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.SelfUserEntity
import kotlinx.serialization.Serializable

@Serializable
data class SelfUserDTO(
    val id: String,
    val nickname: String,
    val login: String,
    val avatarLink: String?,
    val userType: String
)

fun SelfUserDTO.toSelfUserEntity() = SelfUserEntity(
    avatarLink = this.avatarLink,
    id = this.id,
    login = this.login,
    nickname = this.nickname,
    userType = this.userType
)