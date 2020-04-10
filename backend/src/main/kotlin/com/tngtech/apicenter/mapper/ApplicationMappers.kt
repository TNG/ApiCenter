package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity

fun ApplicationEntity.toDto() = ApplicationDto(this.id, this.name, this.description, this.contact)

fun ApplicationDto.toEntity() = ApplicationEntity(this.id, this.name, this.description, this.contact)
