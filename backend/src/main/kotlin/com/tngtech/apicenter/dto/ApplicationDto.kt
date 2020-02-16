package com.tngtech.apicenter.dto

import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class ApplicationDto(
        val id: String?,
        @NotBlank val name: String,
        @NotBlank val description: String,
        @NotBlank val contact: String
)
