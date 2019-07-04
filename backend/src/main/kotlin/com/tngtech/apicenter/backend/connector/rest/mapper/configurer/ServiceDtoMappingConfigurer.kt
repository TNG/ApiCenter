package com.tngtech.apicenter.backend.connector.rest.mapper.configurer

import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.mapper.converter.ServiceDtoConverter
import com.tngtech.apicenter.backend.domain.entity.Service
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class ServiceDtoMappingConfigurer constructor(private val serviceDtoConverter: ServiceDtoConverter): OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(serviceDtoConverter)

        orikaMapperFactory.classMap(Service::class.java, ServiceDto::class.java)
            .field("id.id", "id")
            .byDefault()
            .register()
    }
}