package com.tngtech.apicenter.backend.connector.rest.dto

data class ServiceDto(
        val id: String,
        val title: String,
        val description: String?,
        val specifications: List<SpecificationDto>,
        val remoteAddress: String?,
        val canEdit: Boolean
)