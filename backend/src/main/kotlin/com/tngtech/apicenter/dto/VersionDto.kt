package com.tngtech.apicenter.dto

import java.util.UUID

data class VersionDto(
    val id: UUID,
    val title: String,
    val version: String,
    val description: String?,
    val content: String,
    val interfaceId: UUID?
)
