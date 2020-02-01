package com.tngtech.apicenter.entity

import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class ApplicationEntity(
        @Id val applicationId: String?,
        @NotBlank val name: String,
        @NotBlank val description: String,
        @NotBlank val contact: String
)
