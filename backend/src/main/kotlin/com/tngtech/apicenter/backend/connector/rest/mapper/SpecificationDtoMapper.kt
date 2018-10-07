package com.tngtech.apicenter.backend.connector.rest.mapper

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class SpecificationDtoMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(specificationFileDto: SpecificationFileDto): Specification =
        mapperFacade.map(specificationFileDto, Specification::class.java)

    fun fromDomain(specification: Specification): SpecificationDto =
        mapperFacade.map(specification, SpecificationDto::class.java)
}