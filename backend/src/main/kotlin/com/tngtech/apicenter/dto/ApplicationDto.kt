package com.tngtech.apicenter.dto

import java.util.*
import javax.validation.constraints.NotBlank

data class ApplicationDto(
    val id: UUID?,
    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotBlank val contact: String
)
