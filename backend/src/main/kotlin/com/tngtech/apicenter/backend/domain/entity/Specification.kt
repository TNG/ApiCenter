package com.tngtech.apicenter.backend.domain.entity

import java.util.UUID

data class Specification(
    val id: UUID,
    val title: String,
    val description: String?,
    val versions: List<Version>,
    val remoteAddress: String?
)