package com.tngtech.apicenter.backend.connector.rest.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import java.util.UUID

data class SpecificationMetaData constructor(
    val title: String,
    val version: String,
    val description: String?,
    val language: ApiLanguage,
    val servers: List<String>? = null
)

data class SpecificationFileDto @JsonCreator constructor(
    val fileContent: String?,
    val fileUrl: String? = "",
    val metaData: SpecificationMetaData? = null,
    val id: UUID? = null
)