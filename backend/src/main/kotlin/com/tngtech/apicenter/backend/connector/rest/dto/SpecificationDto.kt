package com.tngtech.apicenter.backend.connector.rest.dto

import java.util.UUID

data class SpecificationDto(
    val id: UUID,
    val title: String,
    val description: String?,
    val versions: List<VersionDto>,
    val remoteAddress: String?
)