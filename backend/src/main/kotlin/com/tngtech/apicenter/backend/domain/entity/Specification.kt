package com.tngtech.apicenter.backend.domain.entity

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetadata

data class Specification(val content: String, val metadata: SpecificationMetadata)