package com.tngtech.apicenter.backend.connector.rest.mapper

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class SpecificationDtoMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(specificationDto: SpecificationDto): Specification =
        mapperFacade.map(specificationDto, Specification::class.java)

    fun fromDomain(specification: Specification): SpecificationDto =
        mapperFacade.map(specification, SpecificationDto::class.java)

}