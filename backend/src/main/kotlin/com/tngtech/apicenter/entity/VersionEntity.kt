package com.tngtech.apicenter.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.NotBlank

@Entity(name = "version")
data class VersionEntity(
    @Id @GeneratedValue val id: UUID,
    @field:NotBlank val title: String,
    @field:NotBlank val version: String,
    val description: String?,
    @field:NotBlank val content: String,

    @ManyToOne
@JoinColumn(name = "interface_id")
val interfaceEntity: InterfaceEntity
)
