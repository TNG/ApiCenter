package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field
import org.springframework.stereotype.Indexed
import java.util.Date
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Entity
@Indexed
@Table(
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["version", "specification_id"])
    ]
)
data class VersionEntity(
    @Id @GeneratedValue val id: UUID,
    @ContainedIn @ManyToOne var specification: SpecificationEntity?,
    @Field val version: String,
    @Field @Column(columnDefinition = "TEXT") val content: String,
    var created: Date?
) {
    @PrePersist
    private fun prePersist() {
        created = Date()
    }
}