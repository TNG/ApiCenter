package com.tngtech.apicenter.backend.connector.database.mapper

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.domain.entity.Service
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class ServiceEntityMapper(private val mapperFacade: MapperFacade) {

    fun toDomain(serviceEntity: ServiceEntity): Service =
        mapperFacade.map(serviceEntity, Service::class.java)

    fun fromDomain(service: Service): ServiceEntity =
        mapperFacade.map(service, ServiceEntity::class.java)

}
