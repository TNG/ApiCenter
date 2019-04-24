package com.tngtech.apicenter.backend.connector.rest.dto

import com.tngtech.apicenter.backend.domain.entity.SpecificationMetadata

data class SpecificationDto(
    val content: String,
    val metadata: SpecificationMetadata
)