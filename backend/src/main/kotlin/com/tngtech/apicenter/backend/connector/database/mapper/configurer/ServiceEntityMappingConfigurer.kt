package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import com.tngtech.apicenter.backend.connector.database.mapper.converter.ServiceEntityConverter
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class ServiceEntityMappingConfigurer(private val serviceEntityConverter: ServiceEntityConverter) : OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(serviceEntityConverter)
    }
}
