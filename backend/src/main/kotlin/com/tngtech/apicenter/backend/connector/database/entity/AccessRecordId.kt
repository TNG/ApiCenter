package com.tngtech.apicenter.backend.connector.database.entity

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class AccessRecordId(
        @Column(nullable = false, name = "service_id")
        val serviceId: String,

        @Column(nullable = false, name = "user_id")
        val userId: UUID
) : Serializable
