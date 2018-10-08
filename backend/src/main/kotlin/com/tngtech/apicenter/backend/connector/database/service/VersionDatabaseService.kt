package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.mapper.VersionEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.VersionRepository
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class VersionDatabaseService constructor(
    private val versionRepository: VersionRepository,
    private val versionEntityMapper: VersionEntityMapper
) : VersionPersistenceService {

    override fun findOne(id: UUID): Version =
        versionRepository.findById(id).map { version -> versionEntityMapper.toDomain(version) }.get()
}