package com.tngtech.apicenter.entity

import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity(name = "interface")
data class InterfaceEntity(
    @Id @GeneratedValue val id: UUID?,
    @field:NotBlank val name: String?,
    val description: String?,
    @field:NotNull val type: InterfaceTypeEntity?,

    @ManyToOne
    @JoinColumn(name = "application_id")
    val application: ApplicationEntity?,

    @OneToMany(mappedBy = "interfaceEntity", cascade = [CascadeType.REMOVE])
    val versions: List<VersionEntity>
)
