package com.application.timer_dmb.domain.entity.receive

data class ReceiverEntity(
    val id: String,
    val receiverAvatarLink: String?,
    val receiverId: String,
    val receiverName: String,
    val receiverNickname: String?
)
