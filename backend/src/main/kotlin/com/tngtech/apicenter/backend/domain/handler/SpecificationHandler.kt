package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SpecificationHandler @Autowired constructor(private val specificationPersistenceService: SpecificationPersistenceService) {

    fun store(specification: Specification) {
        specificationPersistenceService.save(specification)
    }

    fun findAll() = specificationPersistenceService.findAll()

    fun findOne(id: UUID) = specificationPersistenceService.findOne(id)

    fun delete(id: UUID) = specificationPersistenceService.delete(id)

    fun exists(id: UUID) = specificationPersistenceService.exists(id)

    fun search(searchString: String) = specificationPersistenceService.search(searchString)
}