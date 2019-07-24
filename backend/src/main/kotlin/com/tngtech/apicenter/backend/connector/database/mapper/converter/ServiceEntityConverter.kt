package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.domain.entity.Service
import ma.glasnost.orika.converter.BidirectionalConverter
import org.springframework.stereotype.Component
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type


@Component
class ServiceEntityConverter : BidirectionalConverter<Service, ServiceEntity>() {

    override fun convertFrom(source: ServiceEntity, destinationType: Type<Service>?, mappingContext: MappingContext?): Service {
        val specifications = source.specifications.map { spec -> mapperFacade.map(spec, Specification::class.java) }
        return Service(id = ServiceId(source.id), description = source.description, remoteAddress = source.remoteAddress, specifications = specifications, title = source.title)
    }

    override fun convertTo(source: Service, destinationType: Type<ServiceEntity>?, mappingContext: MappingContext?): ServiceEntity {
        val specifications = source.specifications.map { spec -> mapperFacade.map(spec, SpecificationEntity::class.java) }
        return ServiceEntity(id = source.id.id, title = source.title, specifications = specifications, remoteAddress = source.remoteAddress, description = source.description, accessRecords = setOf())
    }
}
