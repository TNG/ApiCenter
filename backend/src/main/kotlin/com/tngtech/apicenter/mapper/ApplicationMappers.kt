package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity
import com.tngtech.apicenter.entity.InterfaceEntity

fun ApplicationEntity.toDto() = ApplicationDto(this.id, this.name, this.description, this.contact, this.interfaces.filter { it.id !== null }.map { it.id!! })

fun ApplicationDto.toEntity() = ApplicationEntity(
        this.id,
        this.name,
        this.description,
        this.contact,
        this.interfaceIds?.map { InterfaceEntity(it, null, null, null, null, emptyList()) }.orEmpty()
)
