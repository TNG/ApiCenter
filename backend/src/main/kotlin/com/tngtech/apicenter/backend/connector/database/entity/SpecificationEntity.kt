package com.tngtech.apicenter.backend.connector.database.entity

import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ReleaseType
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
data class SpecificationEntity(
        @EmbeddedId @IndexedEmbedded val specificationId: SpecificationId,
        @Field @Column(columnDefinition = "TEXT") val content: String,
        @Field @Column(columnDefinition = "TEXT") val title: String,
        @Field @Column(columnDefinition = "TEXT") val description: String?,
        @Field @Column(columnDefinition = "TEXT") val language: ApiLanguage,
        @Field @Column(columnDefinition = "TEXT") val releaseType: ReleaseType,
        @Field @Column(columnDefinition = "TEXT") val endpointUrl: String?,
        @ContainedIn @ManyToOne @MapsId("serviceId") var service: ServiceEntity?,
        var created: Date?
) {
    @PrePersist
    private fun prePersist() {
        created = Date()
    }
}