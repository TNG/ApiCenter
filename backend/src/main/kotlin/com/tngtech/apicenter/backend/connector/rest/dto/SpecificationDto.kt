package com.tngtech.apicenter.backend.connector.rest.dto

import java.util.UUID

data class SpecificationDto(
    val id: UUID,
    val title: String,
    val version: String,
    val content: String,
    val remoteAddress: String?
)