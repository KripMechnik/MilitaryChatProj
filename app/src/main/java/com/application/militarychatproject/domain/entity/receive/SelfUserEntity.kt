package com.application.militarychatproject.domain.entity.receive

data class SelfUserEntity(
    val avatarImageName: String?,
    val id: String,
    val login: String,
    val name: String,
    val nickname: String,
    val online: String,
    val userType: String
)
