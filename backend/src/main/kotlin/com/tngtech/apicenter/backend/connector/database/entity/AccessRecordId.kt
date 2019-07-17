package com.tngtech.apicenter.backend.connector.database.entity

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AccessRecordId(
        @Column(nullable = false, name = "service_id")
        val serviceId: String,
        val userId: UUID
) : Serializable
