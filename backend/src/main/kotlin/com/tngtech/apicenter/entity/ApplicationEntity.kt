package com.tngtech.apicenter.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import org.hibernate.annotations.GenericGenerator
import java.util.*

@Entity
data class ApplicationEntity(
    @Id @GeneratedValue val id: UUID?,
    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotBlank val contact: String
)
