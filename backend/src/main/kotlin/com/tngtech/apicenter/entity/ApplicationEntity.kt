package com.tngtech.apicenter.entity

import java.util.UUID
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class ApplicationEntity(
    @Id @GeneratedValue val id: UUID?,
    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotBlank val contact: String
)
