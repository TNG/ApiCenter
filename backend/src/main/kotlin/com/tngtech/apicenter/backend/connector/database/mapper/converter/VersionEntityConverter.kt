package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.converter.BidirectionalConverter
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class VersionEntityConverter : BidirectionalConverter<Version, VersionEntity>() {

    override fun convertFrom(source: VersionEntity, destinationType: Type<Version>?, mappingContext: MappingContext?): Version {
        return Version(
            source.content,
            VersionMetaData(
                    ServiceId(source.versionId.specificationId),
                    source.title,
                    source.versionId.version,
                    source.description,
                    source.language,
                    source.endpointUrl
            )
        )
    }

    override fun convertTo(source: Version, destinationType: Type<VersionEntity>?, mappingContext: MappingContext?): VersionEntity {
        return VersionEntity(VersionId(
                        source.metadata.id.id,
                        source.metadata.version),
                        source.content,
                        source.metadata.title,
                        source.metadata.description,
                        source.metadata.language,
                        source.metadata.endpointUrl,
                        null,
                        null
        )
    }
}