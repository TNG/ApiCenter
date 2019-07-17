package com.tngtech.apicenter.backend.connector.database.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class UserEntity(
    @Id val id: UUID,
    val username: String,
    val email: String,
    val origin: String,
    val externalId: String)