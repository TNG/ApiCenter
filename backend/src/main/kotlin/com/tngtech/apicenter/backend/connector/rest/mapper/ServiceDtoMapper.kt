package com.tngtech.apicenter.backend.connector.rest.mapper

import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.domain.entity.Service
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class ServiceDtoMapper(private val mapperFacade: MapperFacade) {

    fun toDomain(serviceDto: ServiceDto): Service =
        mapperFacade.map(serviceDto, Service::class.java)

    fun fromDomain(service: Service): ServiceDto =
        mapperFacade.map(service, ServiceDto::class.java)

}
