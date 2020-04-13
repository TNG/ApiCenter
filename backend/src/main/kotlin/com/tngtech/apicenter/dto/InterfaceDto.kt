package com.tngtech.apicenter.dto

import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class InterfaceDto(
    val id: UUID?,
    @field:NotBlank val name: String?,
    val description: String?,
    @field:NotNull val type: InterfaceTypeDto?,

    @field:NotNull val applicationId: UUID?
)
