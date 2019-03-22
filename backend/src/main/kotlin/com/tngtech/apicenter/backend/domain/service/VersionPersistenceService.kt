package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.Version

interface VersionPersistenceService {
    fun findOne(specificationId: String, versionTitle: String): Version?
    fun delete(specificationId: String, versionTitle: String)
}