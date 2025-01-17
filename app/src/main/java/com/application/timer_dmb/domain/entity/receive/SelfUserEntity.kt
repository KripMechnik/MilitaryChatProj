package com.application.timer_dmb.domain.entity.receive

data class SelfUserEntity(
    val avatarLink: String?,
    val id: String,
    val login: String,
    val nickname: String,
    val userType: String
)
