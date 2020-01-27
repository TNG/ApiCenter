package com.tngtech.apicenter.entity

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class ApplicationEntity(
    @Id val applicationId: String?,
    val name: String,
    val description: String,
    val contact: String
)
