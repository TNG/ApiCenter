package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.config.VersionAlreadyExistsException
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.mapper.SpecificationEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.hibernate.search.jpa.Search
import org.hibernate.search.exception.EmptyQueryException
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityManager
import javax.transaction.Transactional
import org.springframework.dao.DataIntegrityViolationException

@Service
class SpecificationDatabaseService constructor(
    private val specificationRepository: SpecificationRepository,
    private val entityManager: EntityManager,
    private val specificationEntityMapper: SpecificationEntityMapper
) : SpecificationPersistenceService {

    override fun save(specification: Specification) {
        val specificationEntity = specificationEntityMapper.fromDomain(specification)

        if (specificationRepository.existsByTitle(specificationEntity.title)) {
            val existingEntity = specificationRepository.findByTitle(specificationEntity.title)

            val versions = existingEntity.versions + specificationEntity.versions
            val newEntity = SpecificationEntity(existingEntity.id, existingEntity.title, existingEntity.description, versions, existingEntity.remoteAddress)

            mapAndStoreEntity(newEntity)
        } else {
            mapAndStoreEntity(specificationEntity)
        }
    }

    override fun findAll(): List<Specification> = specificationRepository.findAll().map { spec -> specificationEntityMapper.toDomain(spec) }

    override fun findOne(id: UUID): Specification? =
        specificationRepository.findById(id).orElse(null)?.let { spec -> specificationEntityMapper.toDomain(spec) }

    override fun delete(id: UUID) = specificationRepository.deleteById(id)

    override fun exists(id: UUID): Boolean =
        specificationRepository.existsById(id)

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
}