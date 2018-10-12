package com.tngtech.apicenter.backend.connector.rest.mapper

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class VersionDtoMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(versionDto: VersionDto): Version =
        mapperFacade.map(versionDto, Version::class.java)

    fun fromDomain(version: Version): VersionDto =
        mapperFacade.map(version, VersionDto::class.java)

}