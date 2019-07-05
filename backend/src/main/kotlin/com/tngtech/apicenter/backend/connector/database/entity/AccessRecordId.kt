package com.tngtech.apicenter.backend.connector.database.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class AccessRecordId(
        val serviceId: String,
        val userId: String
) : Serializable
