package com.tngtech.apicenter.backend.connector.rest.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId

data class VersionMetaData constructor(
    val id: ServiceId,
    val title: String,
    val version: String,
    val description: String?,
    val language: ApiLanguage,
    val endpointUrl: String? = null
)

data class VersionFileDto @JsonCreator constructor(
    val fileContent: String?,
    val fileUrl: String? = "",
    val metaData: VersionMetaData? = null,
    val id: String? = null
)