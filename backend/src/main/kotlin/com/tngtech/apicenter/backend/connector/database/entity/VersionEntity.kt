package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.ContainedIn
import org.hibernate.search.annotations.Field
import org.springframework.stereotype.Indexed
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
@Indexed
data class VersionEntity(
    @Id @GeneratedValue val id: UUID,
    @ContainedIn @ManyToOne var specification: SpecificationEntity?,
    @Field val version: String,
    @Field @Column(columnDefinition = "TEXT") val content: String
)