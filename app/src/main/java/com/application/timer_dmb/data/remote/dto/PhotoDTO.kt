package com.application.timer_dmb.data.remote.dto

import com.application.timer_dmb.domain.entity.receive.PhotoEntity
import kotlinx.serialization.Serializable

@Serializable
data class PhotoDTO(
    val avatarLink: String
)

fun PhotoDTO.toPhotoEntity() = PhotoEntity(
    avatarLink = this.avatarLink
)