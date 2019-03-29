package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.converter.BidirectionalConverter
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class SpecificationEntityConverter: BidirectionalConverter<Specification, SpecificationEntity>() {

    override fun convertFrom(source: SpecificationEntity, destinationType: Type<Specification>?, mappingContext: MappingContext): Specification {
        return Specification(
                ServiceId(source.id),
                source.title,
                source.description,
                source.versions.map { versionEntity -> mapperFacade.map(versionEntity, Version::class.java) },
                source.remoteAddress
        )
    }

    override fun convertTo(source: Specification, destinationType: Type<SpecificationEntity>?, mappingContext: MappingContext?): SpecificationEntity {
        return SpecificationEntity(
                source.id.id,
                source.title,
                source.description,
                source.versions.map { version -> mapperFacade.map(version, VersionEntity::class.java) },
                source.remoteAddress
        )
    }
}