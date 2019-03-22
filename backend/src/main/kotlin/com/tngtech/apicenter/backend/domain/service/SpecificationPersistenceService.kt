package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.Specification

interface SpecificationPersistenceService {
    fun save(specification: Specification)
    fun findAll(): List<Specification>
    fun findOne(id: String): Specification?
    fun delete(id: String)
    fun exists(id: String): Boolean
    fun search(searchString: String): List<Specification>
}