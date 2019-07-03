package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.connector.database.mapper.ServiceEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.ServiceRepository
import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationAlreadyExistsException
import com.tngtech.apicenter.backend.domain.service.ServicePersistor
import org.hibernate.search.exception.EmptyQueryException
import org.hibernate.search.jpa.Search
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import javax.persistence.EntityManager
import javax.transaction.Transactional

@org.springframework.stereotype.Service
class ServiceDatabase constructor(
        private val serviceRepository: ServiceRepository,
        private val entityManager: EntityManager,
        private val serviceEntityMapper: ServiceEntityMapper
) : ServicePersistor {

    override fun save(service: Service) {
        val serviceEntity = serviceEntityMapper.fromDomain(service)
        serviceEntity.specifications.map { specificationEntity -> specificationEntity.service = serviceEntity }

        try {
            serviceRepository.save(serviceEntity)
        } catch (sqlException: DataIntegrityViolationException) {
            throw SpecificationAlreadyExistsException(serviceEntity.title)
        }
    }

    override fun findAll(pageable: Pageable): Page<Service> =
        serviceRepository.findAll(pageable).map { spec -> serviceEntityMapper.toDomain(spec) }

    override fun findOne(id: ServiceId): Service? =
        serviceRepository.findById(id.id).orElse(null)?.let { spec -> serviceEntityMapper.toDomain(spec) }

    override fun delete(id: ServiceId) = serviceRepository.deleteById(id.id)

    override fun exists(id: ServiceId): Boolean =
        serviceRepository.existsById(id.id)

    @Transactional
    override fun search(searchString: String): List<Service> {
        val fullTextEntityManager = Search.getFullTextEntityManager(entityManager)

        val specificationQueryBuilder =
            fullTextEntityManager.searchFactory.buildQueryBuilder().forEntity(ServiceEntity::class.java).get()
        return try {
            val specificationQuery =
                    specificationQueryBuilder.keyword().onFields("title", "description", "specifications.content").matching(searchString)
                            .createQuery()
            val hibernateQuery =
                    fullTextEntityManager.createFullTextQuery(specificationQuery, ServiceEntity::class.java)

            hibernateQuery.resultList.map { it -> it as ServiceEntity }.map { it -> serviceEntityMapper.toDomain(it) }
        } catch (ex: EmptyQueryException) {
            listOf()
        }
    }


}