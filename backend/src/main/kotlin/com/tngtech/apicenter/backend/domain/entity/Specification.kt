package com.tngtech.apicenter.backend.domain.entity

import java.util.regex.Pattern

enum class ReleaseType {
    RELEASE, PRERELEASE, SNAPSHOT
}

fun inferReleaseType(version: String): ReleaseType {
    return if (version.endsWith("-SNAPSHOT")) {
        ReleaseType.SNAPSHOT
    } else if (Pattern.matches("-BETA\\d*", version) || Pattern.matches("-RC\\d*", version)) {
        ReleaseType.PRERELEASE
    } else {
        ReleaseType.RELEASE
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