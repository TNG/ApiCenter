package com.tngtech.apicenter.backend.domain.entity

enum class ApiLanguage {
    OPENAPI, GRAPHQL
}

data class Specification(
    val id: ServiceId,
    val title: String,
    val description: String?,
    val versions: List<Version>,
    val remoteAddress: String?
)