package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.Specification
import java.util.UUID

interface SpecificationPersistenceService {
    fun save(specification: Specification)
    fun findAll(): List<Specification>
    fun findOne(id: UUID): Specification?
    fun delete(id: UUID)
    fun exists(id: UUID): Boolean
    fun search(searchString: String): List<Specification>
}