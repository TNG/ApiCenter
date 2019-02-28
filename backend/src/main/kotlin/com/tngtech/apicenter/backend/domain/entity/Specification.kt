package com.tngtech.apicenter.backend.domain.entity

import java.util.UUID

enum class ApiLanguage {
    OPENAPI, GRAPHQL
}

data class Specification(
    val id: UUID,
    val title: String,
    val description: String?,
    val versions: List<Version>,
    val remoteAddress: String?
)