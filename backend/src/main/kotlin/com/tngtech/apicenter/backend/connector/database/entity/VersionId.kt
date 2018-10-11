package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import java.io.Serializable
import java.util.UUID
import javax.persistence.Embeddable
import javax.persistence.ManyToOne

@Embeddable
data class VersionId(
    val specificationId: UUID?,
    val version: String
) : Serializable