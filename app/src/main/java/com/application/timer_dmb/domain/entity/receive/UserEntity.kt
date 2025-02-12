package com.application.timer_dmb.domain.entity.receive

data class UserEntity (
    val id: Int?,
    val name: String,
    val nickname: String,
    val avatarImageName: String? = null,
    val isAdmin: Boolean,
    val userType: String
)
