package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import com.tngtech.apicenter.backend.domain.entity.Version
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class VersionHandler constructor(private val versionPersistenceService: VersionPersistenceService) {

    fun findOne(specificationId: UUID, version: String): Version? = versionPersistenceService.findOne(specificationId, version)

    fun delete(specificationId: UUID, version: String) = versionPersistenceService.delete(specificationId, version)

}