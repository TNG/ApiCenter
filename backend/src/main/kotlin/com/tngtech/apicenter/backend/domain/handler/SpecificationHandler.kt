package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.exceptions.PreexistingVersionContentDiscrepancyException
import com.tngtech.apicenter.backend.domain.exceptions.PreexistingVersionContentIdenticalException
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class SpecificationHandler @Autowired constructor(
        private val specificationPersistenceService: SpecificationPersistenceService
) {

    fun saveOne(version: Version, serviceId: ServiceId, fileUrl: String?) {
        val existingSpec: Specification? = specificationPersistenceService.findOne(serviceId)

        if (existingSpec == null) {
            storeNewSpecification(version, serviceId, fileUrl)
        } else {
            updateExistingSpecification(existingSpec, version)
        }
    }

    private fun storeNewSpecification(version: Version, serviceId: ServiceId, fileUrl: String?) {
        val newSpecification = Specification(
                serviceId,
                version.metadata.title,
                version.metadata.description,
                listOf(version),
                fileUrl
        )
        specificationPersistenceService.save(newSpecification)
    }

    private fun updateExistingSpecification(specification: Specification, version: Version) {
        val versionStrings = specification.versions.map { v -> v.metadata.version }
        val newVersionString = version.metadata.version
        if (versionStrings.contains(newVersionString)) {

            val existingContents = specification.versions.firstOrNull {
                v -> v.metadata.version == newVersionString
            }?.content

            if (existingContents == version.content) {
                throw PreexistingVersionContentIdenticalException()
            } else {
                throw PreexistingVersionContentDiscrepancyException()
            }
        } else {
            specificationPersistenceService.save(specification.appendVersion(version))
        }

    }

    fun findAll(): List<Specification> = specificationPersistenceService.findAll()

    fun findOne(id: ServiceId): Specification? = specificationPersistenceService.findOne(id)

    fun delete(id: ServiceId) = specificationPersistenceService.delete(id)

    fun exists(id: ServiceId): Boolean = specificationPersistenceService.exists(id)

    fun search(searchString: String): List<Specification> = specificationPersistenceService.search(searchString)
}