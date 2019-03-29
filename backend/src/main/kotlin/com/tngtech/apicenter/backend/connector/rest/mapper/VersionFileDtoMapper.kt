package com.tngtech.apicenter.backend.connector.rest.mapper

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class VersionFileDtoMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(versionFileDto: VersionFileDto): Version =
        mapperFacade.map(versionFileDto, Version::class.java)

    fun fromDomain(version: Version): VersionDto =
        mapperFacade.map(version, VersionDto::class.java)
}