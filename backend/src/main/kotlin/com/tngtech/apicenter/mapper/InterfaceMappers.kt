package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.InterfaceDto
import com.tngtech.apicenter.dto.InterfaceTypeDto
import com.tngtech.apicenter.entity.InterfaceEntity
import com.tngtech.apicenter.entity.InterfaceTypeEntity

fun InterfaceEntity.toDto() = InterfaceDto(this.id, this.name, this.description, InterfaceTypeDto.valueOf(this.type.toString()))

fun InterfaceDto.toEntity() = InterfaceEntity(this.id, this.name, this.description, InterfaceTypeEntity.valueOf(this.type.toString()))
