package com.tngtech.apicenter.dto

import javax.validation.constraints.NotBlank

data class ApplicationDto(
    val id: String?,
    @NotBlank val name: String,
    @NotBlank val description: String,
    @NotBlank val contact: String
)
