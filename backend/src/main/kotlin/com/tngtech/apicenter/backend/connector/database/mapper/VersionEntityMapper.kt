package com.tngtech.apicenter.backend.connector.database.mapper

import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class VersionEntityMapper constructor(private val mapperFacade: MapperFacade) {

    fun toDomain(versionEntity: VersionEntity): Version =
        mapperFacade.map(versionEntity, Version::class.java)

    fun fromDomain(version: Version): VersionEntity =
        mapperFacade.map(version, VersionEntity::class.java)

}