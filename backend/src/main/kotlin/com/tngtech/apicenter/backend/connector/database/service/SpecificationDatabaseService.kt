package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.hibernate.search.jpa.Search
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityManager
import javax.transaction.Transactional

@Service
class SpecificationDatabaseService constructor(
    private val specificationRepository: SpecificationRepository,
    private val entityManager: EntityManager
) : SpecificationPersistenceService {

    override fun save(specification: Specification) {
        specificationRepository.save(SpecificationEntity.fromDomain(specification))
    }

    override fun findAll(): List<Specification> = specificationRepository.findAll().map { spec -> spec.toDomain() }

    override fun findOne(id: UUID): Specification? =
        specificationRepository.findById(id).map { spec -> spec.toDomain() }.get()

    override fun delete(id: UUID) = specificationRepository.deleteById(id)

    override fun exists(id: UUID): Boolean =
        specificationRepository.existsById(id)

    @Transactional
    override fun search(searchString: String): List<Specification> {
        val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)

        val specificationQueryBuilder =
            fullTextEntityManager.searchFactory.buildQueryBuilder().forEntity(SpecificationEntity::class.java).get()
        val specificationQuery =
            specificationQueryBuilder.keyword().onFields("title", "version", "content").matching(searchString)
                .createQuery()

        val hibernateQuery =
            fullTextEntityManager.createFullTextQuery(specificationQuery, SpecificationEntity::class.java)

        return hibernateQuery.resultList.map { it -> it as SpecificationEntity }.map { it -> it.toDomain() }
    }
}