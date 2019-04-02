package com.tngtech.apicenter.backend.connector.rest.mapper.configurer

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.converter.SpecificationDtoConverter
import com.tngtech.apicenter.backend.connector.rest.mapper.converter.SpecificationFileDtoConverter
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class SpecificationFileDtoMappingConfigurer constructor(
        private val specificationFileDtoConverter: SpecificationFileDtoConverter,
        private val specificationDtoConverter: SpecificationDtoConverter
) :
    OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.classMap(Specification::class.java, SpecificationDto::class.java)
            .field("id.id", "id")
            .byDefault()
            .register()

        orikaMapperFactory.classMap(SpecificationFileDto::class.java, Specification::class.java)
            .field("id", "id.id")
            .byDefault()

        orikaMapperFactory.converterFactory.registerConverter(specificationFileDtoConverter)
        orikaMapperFactory.converterFactory.registerConverter(specificationDtoConverter)
    }
}