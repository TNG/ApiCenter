package com.tngtech.apicenter.backend.connector.rest.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId

data class SpecificationMetadata constructor(
    val id: ServiceId,
    val title: String,
    val version: String,
    val description: String?,
    val language: ApiLanguage,
    val endpointUrl: String? = null
)

data class SpecificationFileDto @JsonCreator constructor(
        val fileContent: String?,
        val fileUrl: String? = "",
        val metadata: SpecificationMetadata? = null,
        val id: String? = null
)