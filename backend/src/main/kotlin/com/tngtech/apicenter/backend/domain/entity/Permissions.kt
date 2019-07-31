package com.tngtech.apicenter.backend.domain.entity

data class Permissions(
        val view: Boolean,
        val viewPrereleases: Boolean,
        val edit: Boolean
)
