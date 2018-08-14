package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SpecificationDatabaseService constructor(
    private val specificationRepository: SpecificationRepository
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
}