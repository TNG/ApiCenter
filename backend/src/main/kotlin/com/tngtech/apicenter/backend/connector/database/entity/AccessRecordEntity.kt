package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.*

@Entity
data class AccessRecordEntity(
        @EmbeddedId @IndexedEmbedded val accessRecordId: AccessRecordId,

        @ManyToOne
        @MapsId("serviceId")
        @JoinColumn(name = "id")
        val serviceEntity: ServiceEntity,

        @ManyToOne
        @MapsId(value = "username")
        @JoinColumn(name = "username")
        val userEntity: UserEntity,

        val view: Boolean,
        val viewPrereleases: Boolean,
        val edit: Boolean
)
