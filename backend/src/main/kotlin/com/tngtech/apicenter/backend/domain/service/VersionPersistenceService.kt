package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.Version
import java.util.UUID

interface VersionPersistenceService {
    fun findOne(specificationId: UUID, versionTitle: String): Version
    fun delete(specificationId: UUID, versionTitle: String)
}