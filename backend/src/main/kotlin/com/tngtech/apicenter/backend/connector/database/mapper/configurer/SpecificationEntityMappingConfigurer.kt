package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.mapper.converter.SpecificationEntityConverter
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class SpecificationEntityMappingConfigurer constructor(private val specificationEntityConverter: SpecificationEntityConverter) :
        OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(specificationEntityConverter)

        orikaMapperFactory.classMap(Specification::class.java, SpecificationEntity::class.java)
            .field("id.id", "id")
            .byDefault()
            .register()
    }
}