package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification

interface SpecificationPersistenceService {
    fun save(specification: Specification)
    fun findAll(): List<Specification>
    fun findOne(id: ServiceId): Specification?
    fun delete(id: ServiceId)
    fun exists(id: ServiceId): Boolean
    fun search(searchString: String): List<Specification>
}