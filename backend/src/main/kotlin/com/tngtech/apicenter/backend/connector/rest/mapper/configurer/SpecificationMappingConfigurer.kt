package com.tngtech.apicenter.backend.connector.rest.mapper.configurer

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.converter.SpecificationConverter
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class SpecificationMappingConfigurer constructor(private val specificationConverter: SpecificationConverter) :
    OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.classMap(Specification::class.java, SpecificationDto::class.java)
            .field("version.version", "version")
            .byDefault()
            .register()

        orikaMapperFactory.classMap(SpecificationFileDto::class.java, Specification::class.java)
            .byDefault()

        orikaMapperFactory.converterFactory.registerConverter(specificationConverter)
    }
}