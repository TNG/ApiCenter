package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.ReleaseType
import com.tngtech.apicenter.backend.domain.entity.ResultPage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationConflictException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationDuplicationException
import com.tngtech.apicenter.backend.domain.service.ServicePersistor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServiceHandler @Autowired constructor(
        private val servicePersistor: ServicePersistor
) {

    fun addNewSpecification(specification: Specification, serviceId: ServiceId, fileUrl: String?) {
        val service: Service? = servicePersistor.findOne(serviceId)

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
        servicePersistor.save(service)
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
                if (specification.metadata.releaseType == ReleaseType.SNAPSHOT) {
                    servicePersistor.save(service.overwriteSpecificationContents(specification))
                } else {
                    throw SpecificationConflictException()
                }
            }
        } else {
            servicePersistor.save(service.appendSpecification(specification))
        }

    }

    fun findAll(pageNumber: Int, pageSize: Int): ResultPage<Service> =
            servicePersistor.findAll(pageNumber, pageSize)

    fun findOne(id: ServiceId): Service? = servicePersistor.findOne(id)

    fun delete(id: ServiceId) = servicePersistor.delete(id)

    fun exists(id: ServiceId): Boolean = servicePersistor.exists(id)

    fun search(searchString: String): List<Service> = servicePersistor.search(searchString)
}