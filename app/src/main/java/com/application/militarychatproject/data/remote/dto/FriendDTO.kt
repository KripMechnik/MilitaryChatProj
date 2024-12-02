package com.application.militarychatproject.data.remote.dto

import com.application.militarychatproject.domain.entity.receive.FriendEntity
import kotlinx.serialization.Serializable

@Serializable
data class FriendDTO(
    val avatarLink: String? = null,
    val id: String,
    val name: String,
    val nickname: String
)

fun FriendDTO.toFriendEntity(): FriendEntity = FriendEntity(
    id = this.id,
    avatarLink = this.avatarLink,
    nickname = this.nickname,
    name = this.name
)