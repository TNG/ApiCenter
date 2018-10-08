package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.Version
import java.util.UUID

interface VersionPersistenceService {
    fun findOne(id: UUID): Version
}