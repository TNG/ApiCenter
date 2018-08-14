package com.tngtech.apicenter.backend.domain.entity

import java.util.UUID

data class Specification(
    val id: UUID,
    val title: String,
    val version: Version,
    val content: String,
    val remoteAddress: String?
)