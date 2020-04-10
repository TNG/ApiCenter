package com.tngtech.apicenter.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

@Entity
data class InterfaceEntity(
        @Id @GeneratedValue val id: UUID?,
        @NotBlank val name: String,
        @NotBlank val description: String,
        @NotEmpty val type: InterfaceTypeEntity
)
