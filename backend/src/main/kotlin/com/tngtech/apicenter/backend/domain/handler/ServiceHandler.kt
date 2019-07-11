package com.tngtech.apicenter.backend.domain.handler

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
class ServiceHandler @Autowired constructor(
        private val servicePersistor: ServicePersistor,
        private val remoteServiceUpdater: RemoteServiceUpdater,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider,
        private val permissionsManager: PermissionsManager
) {

    fun addNewSpecification(specification: Specification, serviceId: ServiceId, fileUrl: String?) {
        val service: Service? = servicePersistor.findOne(serviceId)

        if (service == null) {
            saveNewService(specification, serviceId, fileUrl)

            val userId = jwtAuthenticationProvider.getCurrentUser()
            permissionsManager.addPermission(userId, serviceId, PermissionType.VIEW)
            permissionsManager.addPermission(userId, serviceId, PermissionType.VIEWPRERELEASE)
            permissionsManager.addPermission(userId, serviceId, PermissionType.EDIT)
        } else if (canEdit(serviceId)) {
            updateExistingService(service, specification)
        } else {
            PermissionDeniedException(serviceId.id)
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
        val username = jwtAuthenticationProvider.getCurrentUser()
        val page = servicePersistor.findAll(pageNumber, pageSize, username)
        return ResultPage(this.filterByViewPermission(page.content), page.last)
    }

    fun findOne(serviceId: ServiceId): Service? =
        this.filterByViewPermission(listOfNotNull(servicePersistor.findOne(serviceId))).firstOrNull()

    fun exists(serviceId: ServiceId): Boolean = this.findOne(serviceId) != null

    private fun canEdit(serviceId: ServiceId) =
            permissionsManager.hasPermission(jwtAuthenticationProvider.getCurrentUser(), serviceId, PermissionType.EDIT)

    fun delete(serviceId: ServiceId) {
        if (canEdit(serviceId)) {
            servicePersistor.delete(serviceId)

            val userId = jwtAuthenticationProvider.getCurrentUser()
            permissionsManager.clearPermissions(userId, serviceId)
        } else {
            PermissionDeniedException(serviceId.id)
        }
    }

    fun search(searchString: String): List<Service> = this.filterByViewPermission(servicePersistor.search(searchString))

    fun changePermission(serviceId: ServiceId, userId: String, addingPermission: Boolean, permission: PermissionType) {
        if (canEdit(serviceId)) {
            if (addingPermission) {
                permissionsManager.addPermission(userId, serviceId, permission)
            } else {
                permissionsManager.removePermission(userId, serviceId, permission)
            }
        } else {
            PermissionDeniedException(serviceId.id)
        }
    }

    fun getPermissions(serviceId: ServiceId, userId: String): Permissions =
        if (canEdit(serviceId)) {
            val view = permissionsManager.hasPermission(userId, serviceId, PermissionType.VIEW)
            val viewPrereleases = permissionsManager.hasPermission(userId, serviceId, PermissionType.VIEWPRERELEASE)
            val edit = permissionsManager.hasPermission(userId, serviceId, PermissionType.EDIT)
            Permissions(view, viewPrereleases, edit)
        } else {
            Permissions(false, false, false)
        }

    fun synchroniseRemoteService(serviceId: ServiceId) {
        if (canEdit(serviceId)) {
            this.findOne(serviceId)?.let {
               service ->
                val newSpecification = remoteServiceUpdater.synchronize(service)
                this.addNewSpecification(newSpecification, service.id, service.remoteAddress)
            }
        } else {
            PermissionDeniedException(serviceId.id)
        }
    }

    private fun filterByViewPermission(services: List<Service>): List<Service> {
        val userId = jwtAuthenticationProvider.getCurrentUser()

        return services
            .filter { service -> permissionsManager.hasPermission(userId, service.id, PermissionType.VIEW) }
            .map { service ->
                if (permissionsManager.hasPermission(userId, service.id, PermissionType.VIEWPRERELEASE)) {
                    service.removePrereleases()
                } else {
                    service
                }
            }
    }
}