package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
data class AccessRecordEntity(
        @EmbeddedId @IndexedEmbedded val accessRecordId: AccessRecordId,
        val view: Boolean,
        val viewPrereleases: Boolean,
        val edit: Boolean
)
