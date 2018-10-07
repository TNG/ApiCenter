package com.tngtech.apicenter.backend.connector.database.entity

import org.hibernate.search.annotations.Field
import org.hibernate.search.annotations.Indexed
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
@Indexed
data class SpecificationEntity(
    @Id @GeneratedValue val id: UUID,
    @Field val title: String,
    @Field @Column(columnDefinition = "TEXT") val description: String?,
    @OneToMany(mappedBy = "specification", cascade = [CascadeType.ALL]) val versions: List<VersionEntity>,
    val remoteAddress: String?
)