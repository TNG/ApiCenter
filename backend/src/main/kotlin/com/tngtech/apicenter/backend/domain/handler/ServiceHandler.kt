package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationConflictException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationDuplicationException
import com.tngtech.apicenter.backend.domain.service.ServicePersistence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServiceHandler @Autowired constructor(
        private val servicePersistence: ServicePersistence
) {

    fun addNewSpecification(specification: Specification, serviceId: ServiceId, fileUrl: String?) {
        val service: Service? = servicePersistence.findOne(serviceId)

        if (service == null) {
            saveNewService(specification, serviceId, fileUrl)
        } else {
            updateExistingService(service, specification)
        }
    }

    private fun saveNewService(specification: Specification, serviceId: ServiceId, fileUrl: String?) {
        val service = Service(
                serviceId,
                specification.metadata.title,
                specification.metadata.description,
                listOf(specification),
                fileUrl
        )
        servicePersistence.save(service)
    }

    private fun updateExistingService(service: Service, specification: Specification) {
        val versions = service.specifications.map { s -> s.metadata.version }
        val newVersion = specification.metadata.version
        if (versions.contains(newVersion)) {

            val existingContents = service.specifications.firstOrNull {
                s -> s.metadata.version == newVersion
            }?.content

            if (existingContents == specification.content) {
                throw SpecificationDuplicationException()
            } else {
                throw SpecificationConflictException()
            }
        } else {
            servicePersistence.save(service.appendSpecification(specification))
        }

    }

    fun findAll(): List<Service> = servicePersistence.findAll()

    fun findOne(id: ServiceId): Service? = servicePersistence.findOne(id)

    fun delete(id: ServiceId) = servicePersistence.delete(id)

    fun exists(id: ServiceId): Boolean = servicePersistence.exists(id)

    fun search(searchString: String): List<Service> = servicePersistence.search(searchString)
}