package com.tngtech.apicenter.backend.connector.database.mapper.configurer

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.domain.entity.Service
import ma.glasnost.orika.MapperFactory
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer
import org.springframework.stereotype.Component

@Component
class ServiceEntityMappingConfigurer : OrikaMapperFactoryConfigurer {

    override fun configure(orikaMapperFactory: MapperFactory) {
        orikaMapperFactory.classMap(Service::class.java, ServiceEntity::class.java)
            .field("id.id", "id")
            .byDefault()
            .register()
    }
}