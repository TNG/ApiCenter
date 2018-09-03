package com.tngtech.apicenter.backend.connector.database.entity

import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Indexed
data class SpecificationEntity(
    @Id @GeneratedValue val id: UUID,
    @Field val title: String,
    @Field @Column(columnDefinition = "TEXT") val description: String?,
    @Field val version: String,
    @Field @Column(columnDefinition = "TEXT") val content: String,
    val remoteAddress: String?
) {

    companion object {
        fun fromDomain(specification: Specification): SpecificationEntity =
            SpecificationEntity(
                specification.id,
                specification.title,
                specification.description,
                specification.version.version,
                specification.content,
                specification.remoteAddress
            )
    }

    fun toDomain(): Specification = Specification(
        this.id,
        this.title,
        this.description,
        Version(this.version),
        this.content,
        this.remoteAddress
    )
}