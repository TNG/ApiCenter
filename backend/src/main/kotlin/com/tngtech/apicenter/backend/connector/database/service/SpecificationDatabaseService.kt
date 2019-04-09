package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.mapper.SpecificationEntityMapper
import com.tngtech.apicenter.backend.connector.database.mapper.VersionEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.exceptions.PreexistingVersionContentDiscrepancyException
import com.tngtech.apicenter.backend.domain.exceptions.PreexistingVersionContentIdenticalException
import com.tngtech.apicenter.backend.domain.exceptions.VersionAlreadyExistsException
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.hibernate.search.exception.EmptyQueryException
import org.hibernate.search.jpa.Search
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
class SpecificationDatabaseService constructor(
    private val specificationRepository: SpecificationRepository,
    private val entityManager: EntityManager,
    private val specificationEntityMapper: SpecificationEntityMapper,
    private val versionEntityMapper: VersionEntityMapper
) : SpecificationPersistenceService {

    override fun findAll(): List<Specification> = specificationRepository.findAll().map { spec -> specificationEntityMapper.toDomain(spec) }

    override fun findOne(id: ServiceId): Specification? =
        specificationRepository.findById(id.id).orElse(null)?.let { spec -> specificationEntityMapper.toDomain(spec) }

    override fun delete(id: ServiceId) = specificationRepository.deleteById(id.id)

    override fun exists(id: ServiceId): Boolean =
        specificationRepository.existsById(id.id)

    @Transactional
    override fun search(searchString: String): List<Specification> {
        val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)

        val specificationQueryBuilder =
            fullTextEntityManager.searchFactory.buildQueryBuilder().forEntity(SpecificationEntity::class.java).get()
        return try {
            val specificationQuery =
                    specificationQueryBuilder.keyword().onFields("title", "description", "versions.content").matching(searchString)
                            .createQuery()
            val hibernateQuery =
                    fullTextEntityManager.createFullTextQuery(specificationQuery, SpecificationEntity::class.java)

            hibernateQuery.resultList.map { it -> it as SpecificationEntity }.map { it -> specificationEntityMapper.toDomain(it) }
        } catch (ex: EmptyQueryException) {
            listOf()
        }
    }

    private fun mapAndStoreEntity(specificationEntity: SpecificationEntity) {
        specificationEntity.versions.map { versionEntity -> versionEntity.specification = specificationEntity }

        try {
            specificationRepository.save(specificationEntity)
        } catch (sqlException: DataIntegrityViolationException) {
            throw VersionAlreadyExistsException(specificationEntity.title)
        }
    }

    override fun saveOne(version: Version, serviceId: ServiceId, fileUrl: String?) {
        val existingSpec = specificationRepository.findById(serviceId.id)

        if (!existingSpec.isPresent) {
            storeNewSpecification(version, serviceId, fileUrl)
        }

        existingSpec.ifPresent { s -> updateExistingSpecification(s, version) }
    }

    private fun storeNewSpecification(version: Version, serviceId: ServiceId, fileUrl: String?) {
        val newSpecification = Specification(
                serviceId,
                version.metadata.title,
                version.metadata.description,
                listOf(version),
                fileUrl
        )
        val newSpecificationEntity = specificationEntityMapper.fromDomain(newSpecification)
        mapAndStoreEntity(newSpecificationEntity)
    }

    private fun updateExistingSpecification(s: SpecificationEntity, version: Version) {
        val versionStrings = s.versions.map { v -> versionEntityMapper.toDomain(v).metadata.version }
        val newVersionString = version.metadata.version
        if (versionStrings.contains(newVersionString)) {

            val existingContents = s.versions.firstOrNull {
                versionEntity -> versionEntityMapper.toDomain(versionEntity).metadata.version == newVersionString
            }?.content

            if (existingContents == version.content) {
                throw PreexistingVersionContentIdenticalException()
            } else {
                throw PreexistingVersionContentDiscrepancyException()
            }
        } else {
            mapAndStoreEntity(s.appendVersion(versionEntityMapper.fromDomain(version)))
        }

    }
}