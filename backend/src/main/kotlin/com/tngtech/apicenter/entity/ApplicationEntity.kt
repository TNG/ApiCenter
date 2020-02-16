package com.tngtech.apicenter.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import org.hibernate.annotations.GenericGenerator

@Entity
data class ApplicationEntity(
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    val id: String?,

    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotBlank val contact: String
)
