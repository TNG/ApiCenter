package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component
import com.tngtech.apicenter.backend.connector.database.mapper.converter.ServiceEntityConverter

@Component
class ServiceEntityMappingConfigurer constructor(private val serviceEntityConverter: ServiceEntityConverter) : OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(serviceEntityConverter)
    }
}