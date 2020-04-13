package com.tngtech.apicenter.dto

import java.util.UUID
import javax.validation.constraints.NotBlank

data class ApplicationDto(
    val id: UUID?,
    @NotBlank val name: String?,
    @NotBlank val description: String?,
    @NotBlank val contact: String?,

    val interfaceIds: List<UUID>?
)
