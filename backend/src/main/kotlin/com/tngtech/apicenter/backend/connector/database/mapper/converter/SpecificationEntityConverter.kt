package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationId
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.SpecificationMetadata
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.converter.BidirectionalConverter
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class SpecificationEntityConverter : BidirectionalConverter<Specification, SpecificationEntity>() {

    override fun convertFrom(source: SpecificationEntity, destinationType: Type<Specification>?, mappingContext: MappingContext?): Specification {
        return Specification(
            source.content,
            SpecificationMetadata(
                    ServiceId(source.specificationId.serviceId),
                    source.title,
                    source.specificationId.version,
                    source.description,
                    source.language,
                    source.releaseType,
                    source.endpointUrl
            )
        )
    }

    override fun convertTo(source: Specification, destinationType: Type<SpecificationEntity>?, mappingContext: MappingContext?): SpecificationEntity {
        return SpecificationEntity(SpecificationId(
                        source.metadata.id.id,
                        source.metadata.version),
                        source.content,
                        source.metadata.title,
                        source.metadata.description,
                        source.metadata.language,
                        source.metadata.releaseType,
                        source.metadata.endpointUrl,
                        null,
                        null
        )
    }
}