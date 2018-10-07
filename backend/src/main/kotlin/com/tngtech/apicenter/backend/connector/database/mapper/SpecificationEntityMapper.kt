package com.tngtech.apicenter.backend.connector.database.mapper

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class SpecificationEntityMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(specificationEntity: SpecificationEntity): Specification =
        mapperFacade.map(specificationEntity, Specification::class.java)

    fun fromDomain(specification: Specification): SpecificationEntity =
        mapperFacade.map(specification, SpecificationEntity::class.java)

}