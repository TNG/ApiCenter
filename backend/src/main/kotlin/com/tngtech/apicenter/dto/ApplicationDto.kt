package com.tngtech.apicenter.dto

import java.util.UUID
import javax.validation.constraints.NotBlank

data class ApplicationDto(
    val id: UUID?,
    @field:NotBlank val name: String?,
    @field:NotBlank val description: String?,
    @field:NotBlank val contact: String?,

    val interfaceIds: List<UUID>?
)
