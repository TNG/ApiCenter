package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.*

@Entity
@Indexed
data class SpecificationEntity(
    @Id val id: String,
    @Field val title: String,
    @Field @Column(columnDefinition = "TEXT") val description: String?,
    @IndexedEmbedded @OneToMany(
        mappedBy = "specification",
        cascade = [CascadeType.ALL]
    ) @OrderBy("created DESC") val versions: List<VersionEntity>,
    val remoteAddress: String?
)