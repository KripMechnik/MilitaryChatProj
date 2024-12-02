package com.application.militarychatproject.domain.entity.send

import kotlinx.serialization.Serializable

@Serializable
data class UpdatedMessageEntity(
    val text: String
)
