package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.*

@Entity
data class AccessRecordEntity(
        @EmbeddedId @IndexedEmbedded val accessRecordId: AccessRecordId,

        @MapsId("serviceId")
        @ManyToOne
        val serviceEntity: ServiceEntity,

        @MapsId("username")
        @ManyToOne
        val userEntity: UserEntity,

        val view: Boolean,
        val viewPrereleases: Boolean,
        val edit: Boolean
)
