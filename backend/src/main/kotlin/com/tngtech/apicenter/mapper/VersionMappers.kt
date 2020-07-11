package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.VersionDto
import com.tngtech.apicenter.entity.VersionEntity

fun VersionEntity.toDto(): VersionDto = VersionDto(this.id, this.title, this.version, this.description, this.content, this.interfaceEntity.id)
