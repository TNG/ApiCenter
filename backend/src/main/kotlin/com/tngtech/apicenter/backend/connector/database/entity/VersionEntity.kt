package com.tngtech.apicenter.backend.connector.database.entity

import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.IndexedEmbedded
import org.springframework.stereotype.Indexed
import java.util.Date
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.PrePersist
import javax.persistence.Table

@Entity
@Indexed
@Table
data class VersionEntity(
        @EmbeddedId @IndexedEmbedded val versionId: VersionId,
        @Field @Column(columnDefinition = "TEXT") val content: String,
        @Field @Column(columnDefinition = "TEXT") val title: String,
        @Field @Column(columnDefinition = "TEXT") val description: String?,
        @Field @Column(columnDefinition = "TEXT") val language: ApiLanguage,
        @Field @Column(columnDefinition = "TEXT") val server: String?,
        @ContainedIn @ManyToOne @MapsId("specificationId") var specification: SpecificationEntity?,
        var created: Date?
) {
    @PrePersist
    private fun prePersist() {
        created = Date()
    }
}