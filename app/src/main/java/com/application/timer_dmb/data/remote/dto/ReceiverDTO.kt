package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.ReceiverEntity
import kotlinx.serialization.Serializable

@Serializable
data class ReceiverDTO(
    val id: String,
    val receiverAvatarLink: String?,
    val receiverId: String,
    val receiverName: String,
    val receiverNickname: String?
)

fun ReceiverDTO.toReceiverEntity() = ReceiverEntity(
    id = this.id,
    receiverAvatarLink = this.receiverAvatarLink,
    receiverId = this.receiverId,
    receiverName = this.receiverName,
    receiverNickname = this.receiverNickname
)