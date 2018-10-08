package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class VersionHandler constructor(private val versionPersistenceService: VersionPersistenceService) {

    fun findOne(id: UUID) = versionPersistenceService.findOne(id)

    fun delete(id: UUID) = versionPersistenceService.delete(id)

}