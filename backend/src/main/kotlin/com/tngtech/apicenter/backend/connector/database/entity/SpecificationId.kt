package com.tngtech.apicenter.backend.connector.database.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class SpecificationId(
        val serviceId: String,
        val version: String
) : Serializable