package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import com.tngtech.apicenter.backend.connector.database.mapper.converter.SpecificationEntityConverter
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class SpecificationEntityMappingConfigurer(
        private val specificationEntityConverter: SpecificationEntityConverter
) : OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(specificationEntityConverter)
    }
}
