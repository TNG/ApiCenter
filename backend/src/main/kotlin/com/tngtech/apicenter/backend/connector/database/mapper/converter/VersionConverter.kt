package com.tngtech.apicenter.backend.connector.database.mapper.converter

import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class VersionConverter : CustomConverter<Version, VersionEntity>() {

    override fun convert(
        source: Version,
        destinationType: Type<out VersionEntity>,
        mappingContext: MappingContext
    ): VersionEntity = VersionEntity(VersionId(null, source.version), source.content, source.language, null, null)
}