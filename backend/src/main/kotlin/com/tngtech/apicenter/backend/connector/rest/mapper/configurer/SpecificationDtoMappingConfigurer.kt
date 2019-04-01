package com.tngtech.apicenter.backend.connector.rest.mapper.configurer

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class SpecificationDtoMappingConfigurer: OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.classMap(Specification::class.java, SpecificationDto::class.java)
            .field("id.id", "id")
            .byDefault()
            .register()
    }

}