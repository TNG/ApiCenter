package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.connector.database.mapper.VersionEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.connector.database.repository.VersionRepository
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class VersionDatabaseService constructor(
    private val versionRepository: VersionRepository,
    private val specificationRepository: SpecificationRepository,
    private val versionEntityMapper: VersionEntityMapper
) : VersionPersistenceService {

    override fun findOne(specificationId: UUID, versionTitle: String): Version {
        return versionRepository.findById(VersionId(specificationId, versionTitle)).map { version -> versionEntityMapper.toDomain(version) }.get()
    }


    override fun delete(specificationId: UUID, versionTitle: String) {
        versionRepository.deleteById(VersionId(specificationId, versionTitle))
    }
}