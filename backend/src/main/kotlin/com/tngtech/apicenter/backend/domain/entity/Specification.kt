package com.tngtech.apicenter.backend.domain.entity

enum class ReleaseType {
    RELEASE, PRERELEASE, SNAPSHOT
}

data class SpecificationMetadata constructor(
    val id: ServiceId,
    val title: String,
    val version: String,
    val description: String?,
    val language: ApiLanguage,
    val releaseType: ReleaseType,
    val endpointUrl: String? = null
)

data class Specification(val content: String, val metadata: SpecificationMetadata)