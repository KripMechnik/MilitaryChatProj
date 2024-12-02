package com.application.militarychatproject.domain.entity.receive

data class UserEntity (
    val id: Int?,
    val name: String,
    val nickname: String,
    val avatarImageName: String? = null
)
