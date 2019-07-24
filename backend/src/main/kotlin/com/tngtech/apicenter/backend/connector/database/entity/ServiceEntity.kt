package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.*

@Entity
@Indexed
data class ServiceEntity(
        @Id val id: String,
        @Field val title: String,
        @Field @Column(columnDefinition = "TEXT") val description: String?,
        @IndexedEmbedded @OneToMany(
        mappedBy = "service",
        cascade = [CascadeType.ALL]
    ) @OrderBy("created DESC") val specifications: List<SpecificationEntity>,
        val remoteAddress: String?,

        @OneToMany(mappedBy = "serviceEntity", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
        val accessRecords: Set<AccessRecordEntity>
)
