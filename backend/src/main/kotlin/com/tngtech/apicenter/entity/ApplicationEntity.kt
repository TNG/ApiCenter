package com.tngtech.apicenter.entity

import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank

@Entity(name = "application")
data class ApplicationEntity(
    @Id @GeneratedValue val id: UUID?,
    @field:NotBlank val name: String?,
    @field:NotBlank val description: String?,
    @field:NotBlank val contact: String?,

    @OneToMany(mappedBy = "application", cascade = [CascadeType.REMOVE])
    val interfaces: List<InterfaceEntity>
)
