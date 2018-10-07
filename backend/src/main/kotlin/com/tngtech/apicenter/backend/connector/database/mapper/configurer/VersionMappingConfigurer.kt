package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import com.tngtech.apicenter.backend.connector.database.mapper.converter.VersionConverter
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class VersionMappingConfigurer constructor(private val versionConverter: VersionConverter) :
    OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.converterFactory.registerConverter(versionConverter)
    }
}