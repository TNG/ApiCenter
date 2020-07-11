package com.tngtech.apicenter.dto

import java.util.UUID
import javax.validation.constraints.NotBlank

data class VersionFileDto(
    @field:NotBlank val fileContent: String,
    @field:NotBlank val interfaceId: UUID
)
