package com.tngtech.apicenter.backend.connector.rest.dto

import com.tngtech.apicenter.backend.domain.entity.APILanguage

data class VersionDto(
    val version: String,
    val content: String,
    val language: APILanguage
)