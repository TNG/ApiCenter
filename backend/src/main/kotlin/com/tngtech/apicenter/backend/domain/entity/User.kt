package com.tngtech.apicenter.backend.domain.entity

import java.util.UUID

data class User(val id: UUID, val username: String, val email: String, val origin: String, val externalId: String)