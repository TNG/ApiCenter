package com.tngtech.apicenter.backend.domain.entity

enum class ApiLanguage {
    OPENAPI, GRAPHQL
}

class Service(
        val id: ServiceId,
        val title: String,
        val description: String?,
        val specifications: List<Specification>,
        val remoteAddress: String?
) {
    fun appendSpecification(other: Specification): Service {
        val specifications = this.specifications + other
        return Service(
                this.id,
                other.metadata.title,
                other.metadata.description,
                specifications,
                this.remoteAddress
        )
    }
}
