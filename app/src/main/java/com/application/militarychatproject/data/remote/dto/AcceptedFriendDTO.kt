package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.AcceptedFriendEntity
import kotlinx.serialization.Serializable

@Serializable
data class AcceptedFriendDTO(
    val id: String,
    val lastUpdateTime: String,
    val unreadMessagesAmount: String
)

fun AcceptedFriendDTO.toAcceptedFriendEntity() = AcceptedFriendEntity(
    id = this.id,
    lastUpdateTime = this.lastUpdateTime,
    unreadMessagesAmount = this.unreadMessagesAmount
)