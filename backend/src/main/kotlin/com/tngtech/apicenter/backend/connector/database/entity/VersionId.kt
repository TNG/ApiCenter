package com.tngtech.apicenter.backend.connector.database.entity

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class VersionId(
    val specificationId: String?,
    val version: String
) : Serializable