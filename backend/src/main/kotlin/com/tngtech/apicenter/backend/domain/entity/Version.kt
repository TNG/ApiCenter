package com.tngtech.apicenter.backend.domain.entity

import java.util.UUID

data class Version(val id: UUID, val version: String, val content: String)