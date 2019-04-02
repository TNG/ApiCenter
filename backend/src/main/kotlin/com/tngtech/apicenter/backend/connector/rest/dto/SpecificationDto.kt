package com.tngtech.apicenter.backend.connector.rest.dto

data class SpecificationDto(
    val id: String,
    val title: String,
    val description: String?,
    val versions: List<VersionDto>,
    val remoteAddress: String?,
    val showEditButtons: Boolean
)