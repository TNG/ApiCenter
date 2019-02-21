package com.tngtech.apicenter.backend.connector.database.entity

import com.tngtech.apicenter.backend.domain.entity.APILanguage
import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.IndexedEmbedded
import org.springframework.stereotype.Indexed
import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Indexed
@Table
data class VersionEntity(
    @EmbeddedId @IndexedEmbedded val versionId: VersionId,
    @Field @Column(columnDefinition = "TEXT") val content: String,
    @Field @Column(columnDefinition = "TEXT") val language: APILanguage,
    @ContainedIn @ManyToOne @MapsId("specificationId") var specification: SpecificationEntity?,
    var created: Date?
) {
    @PrePersist
    private fun prePersist() {
        created = Date()
    }
}