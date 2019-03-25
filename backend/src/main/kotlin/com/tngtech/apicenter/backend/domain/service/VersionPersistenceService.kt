package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Version

interface VersionPersistenceService {
    fun findOne(specificationId: ServiceId, versionTitle: String): Version?
    fun delete(specificationId: ServiceId, versionTitle: String)
}