package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.SelfUserEntity
import kotlinx.serialization.Serializable

@Serializable
data class SelfUserDTO(
    val id: String,
    val nickname: String,
    val login: String,
    val avatarLink: String?,
    val userType: String,
    val isAdmin: Boolean,
)

fun SelfUserDTO.toSelfUserEntity() = SelfUserEntity(
    avatarLink = this.avatarLink,
    id = this.id,
    login = this.login,
    nickname = this.nickname,
    userType = this.userType,
    isAdmin = this.isAdmin
)