package com.application.militarychatproject.domain.entity.receive

data class SenderEntity(
    val id: String,
    val senderAvatarLink: String?,
    val senderId: String,
    val senderName: String,
    val senderNickname: String?
)
