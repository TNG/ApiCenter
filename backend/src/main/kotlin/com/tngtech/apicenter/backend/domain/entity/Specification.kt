package com.tngtech.apicenter.backend.domain.entity

enum class ApiLanguage {
    OPENAPI, GRAPHQL
}

class Specification(
    val id: ServiceId,
    val title: String,
    val description: String?,
    val versions: List<Version>,
    val remoteAddress: String?
) {
    fun appendVersion(other: Version): Specification {
        val versions = this.versions + other
        return Specification(
                this.id,
                other.metadata.title,
                other.metadata.description,
                versions,
                this.remoteAddress
        )
    }
}
