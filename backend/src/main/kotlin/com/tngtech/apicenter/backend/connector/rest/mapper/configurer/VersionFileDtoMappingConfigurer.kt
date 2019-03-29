package com.tngtech.apicenter.backend.connector.rest.mapper.configurer

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.converter.VersionFileDtoConverter
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class VersionFileDtoMappingConfigurer constructor(private val versionFileDtoConverter: VersionFileDtoConverter) :
    OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(versionFileDtoConverter)

        orikaMapperFactory.classMap(Version::class.java, VersionDto::class.java)
            .field("metadata.id.id", "metadata.id")
            .byDefault()
            .register()

        orikaMapperFactory.classMap(VersionFileDto::class.java, Version::class.java)
            .field("id", "metadata.id.id")
            .byDefault()
            .register()
    }
}