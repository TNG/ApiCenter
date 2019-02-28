package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.converter.BidirectionalConverter
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class VersionConverter : BidirectionalConverter<Version, VersionEntity>() {

    override fun convertFrom(source: VersionEntity, destinationType: Type<Version>?, mappingContext: MappingContext?): Version {
        return Version(
            source.content,
            SpecificationMetaData(
                    source.title,
                    source.versionId.version,
                    source.description,
                    source.language,
                    source.servers.split(',')
            )
        )
    }

    override fun convertTo(source: Version, destinationType: Type<VersionEntity>?, mappingContext: MappingContext?): VersionEntity {
        return VersionEntity(VersionId(
                        null,
                        source.metadata.version),
                        source.content,
                        source.metadata.title,
                        source.metadata.description,
                        source.metadata.language,
                        source.metadata.servers?.let { servers: List<String> -> servers.joinToString() }.orEmpty(),
                        null,
                        null)
    }
}