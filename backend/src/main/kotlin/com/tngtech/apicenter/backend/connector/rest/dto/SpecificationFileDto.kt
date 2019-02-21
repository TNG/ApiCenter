package com.tngtech.apicenter.backend.connector.rest.dto

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.UUID

data class SpecificationMetaData constructor(
    val title: String,
    val version: String,
    val description: String
)

data class SpecificationFileDto @JsonCreator constructor(
    val fileContent: String?,
    val fileUrl: String? = "",
    val metaData: SpecificationMetaData? = null,
    val id: UUID? = null
)