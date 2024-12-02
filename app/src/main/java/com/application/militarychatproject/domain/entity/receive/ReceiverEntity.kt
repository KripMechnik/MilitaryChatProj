package com.application.militarychatproject.domain.entity.receive

data class ReceiverEntity(
    val id: String,
    val receiverAvatarLink: String?,
    val receiverId: String,
    val receiverName: String,
    val receiverNickname: String?
)
