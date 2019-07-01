package com.tngtech.apicenter.backend.domain.entity

import java.util.regex.Pattern

enum class ReleaseType {
    RELEASE, PRERELEASE, SNAPSHOT;

    companion object {
        fun fromVersionString(version: String): ReleaseType {
            return when {
                version.endsWith("-SNAPSHOT") -> SNAPSHOT
                Pattern.matches("-(BETA|RC)\\d*", version) -> PRERELEASE
                else -> RELEASE
            }
        }
    }
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