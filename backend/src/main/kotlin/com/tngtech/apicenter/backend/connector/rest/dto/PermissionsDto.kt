package com.tngtech.apicenter.backend.connector.rest.dto

data class PermissionsDto(
        val view: Boolean,
        val viewPrereleases: Boolean,
        val edit: Boolean
)
