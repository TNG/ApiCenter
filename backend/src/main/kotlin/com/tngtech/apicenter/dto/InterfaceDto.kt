package com.tngtech.apicenter.dto

import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty

data class InterfaceDto(
    val id: UUID?,
    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotEmpty val type: InterfaceTypeDto
)
