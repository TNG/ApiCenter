package com.tngtech.apicenter.backend.connector.rest.dto

import java.util.UUID

data class VersionDto(
    val id: UUID,
    val version: String,
    val content: String
)