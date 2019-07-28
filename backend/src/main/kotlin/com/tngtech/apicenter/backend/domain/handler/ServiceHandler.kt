package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.config.ApiCenterProperties
import com.tngtech.apicenter.backend.domain.entity.ReleaseType
import com.tngtech.apicenter.backend.domain.entity.ResultPage
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.connector.rest.service.RemoteServiceUpdater
import com.tngtech.apicenter.backend.domain.entity.*
import com.tngtech.apicenter.backend.domain.exceptions.PermissionDeniedException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationConflictException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationDuplicationException
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import com.tngtech.apicenter.backend.domain.service.ServicePersistor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class ServiceHandler(
        private val servicePersistor: ServicePersistor,
        private val remoteServiceUpdater: RemoteServiceUpdater,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider,
        private val permissionsManager: PermissionsManager,
        private val apiCenterProperties: ApiCenterProperties
) {

    fun addNewSpecification(specification: Specification, serviceId: ServiceId, fileUrl: String?, isPublic: Boolean) {
        val service: Service? = servicePersistor.findOne(serviceId)

        if (service == null) {
            saveNewService(specification, serviceId, fileUrl)
            val userId = jwtAuthenticationProvider.getCurrentUserId()
            permissionsManager.assignRole(userId, serviceId, Role.EDITOR)
        } else if (canEdit(serviceId)) {
            updateExistingService(service, specification)
        } else {
            throw PermissionDeniedException(serviceId.id)
        }

        if (isPublic) {
            permissionsManager.assignRole(apiCenterProperties.getAnonymousUsername(), serviceId, Role.VIEWER)
        } else {
            permissionsManager.removeRole(apiCenterProperties.getAnonymousUsername(), serviceId)
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

    fun findAll(pageNumber: Int, pageSize: Int): ResultPage<Service> {
        val userId = jwtAuthenticationProvider.getCurrentUserId()
        val page = servicePersistor.findAll(pageNumber, pageSize, userId, apiCenterProperties.getAnonymousUsername())
        return ResultPage(this.filterByViewPermission(page.content), page.last)
    }

    fun findOne(serviceId: ServiceId): Service? =
        this.filterByViewPermission(listOfNotNull(servicePersistor.findOne(serviceId))).firstOrNull()

    fun exists(serviceId: ServiceId): Boolean = this.findOne(serviceId) != null

    private fun canEdit(serviceId: ServiceId) =
            permissionsManager.hasPermission(jwtAuthenticationProvider.getCurrentUserId(), serviceId, PermissionType.EDIT)

    fun delete(serviceId: ServiceId) {
        if (canEdit(serviceId)) {
            servicePersistor.delete(serviceId)
        } else {
            throw PermissionDeniedException(serviceId.id)
        }
    }

    fun search(searchString: String): List<Service> = this.filterByViewPermission(servicePersistor.search(searchString))

    fun assignRole(serviceId: ServiceId, username: String, role: Role) {
        if (canEdit(serviceId)) {
            permissionsManager.assignRole(username, serviceId, role)
        } else {
            throw PermissionDeniedException(serviceId.id)
        }
    }

    fun removeRole(serviceId: ServiceId, username: String) {
        if (canEdit(serviceId)) {
            permissionsManager.removeRole(username, serviceId)
        } else {
            throw PermissionDeniedException(serviceId.id)
        }

    }

    fun getRole(serviceId: ServiceId, username: String): Role? =
        if (canEdit(serviceId)) {
            permissionsManager.getRole(username, serviceId)
        } else {
            null
        }

    fun synchroniseRemoteService(serviceId: ServiceId) {
        if (canEdit(serviceId)) {
            this.findOne(serviceId)?.let {
               service ->
                val newSpecification = remoteServiceUpdater.synchronize(service)
                val isPublic = permissionsManager.hasPermission(apiCenterProperties.getAnonymousUsername(), serviceId, PermissionType.VIEW)
                this.addNewSpecification(newSpecification, service.id, service.remoteAddress, isPublic)
            }
        } else {
            throw PermissionDeniedException(serviceId.id)
        }
    }

    private fun filterByViewPermission(services: List<Service>): List<Service> {
        val userId = jwtAuthenticationProvider.getCurrentUserId()
        // The paged query result only retrieves Services for which the user has at least VIEW permission
        return services
            .map { service ->
                if (!permissionsManager.hasPermission(userId, service.id, PermissionType.VIEWPRERELEASE)) {
                    service.removePrereleases()
                } else {
                    service
                }
            }
    }
}