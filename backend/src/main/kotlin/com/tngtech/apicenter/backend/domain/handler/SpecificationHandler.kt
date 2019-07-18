package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ReleaseType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.PermissionDeniedException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistor
import org.springframework.stereotype.Component

@Component
class SpecificationHandler constructor(
        private val specificationPersistor: SpecificationPersistor,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider,
        private val permissionsManager: PermissionsManager
) {
    fun findOne(serviceId: ServiceId, version: String): Specification? {
        val userId = jwtAuthenticationProvider.getCurrentUserId()

        val specification = specificationPersistor.findOne(serviceId, version)
        val canView = when (specification?.metadata?.releaseType) {
            ReleaseType.PRERELEASE -> permissionsManager.hasPermission(userId, serviceId, PermissionType.VIEWPRERELEASE)
            ReleaseType.SNAPSHOT -> permissionsManager.hasPermission(userId, serviceId, PermissionType.VIEWPRERELEASE)
            ReleaseType.RELEASE -> permissionsManager.hasPermission(userId, serviceId, PermissionType.VIEW)
            else -> false
        }

        return if (canView) {
            specification
        } else {
            throw SpecificationNotFoundException(serviceId.id, version)
        }
    }

    fun delete(serviceId: ServiceId, version: String) {
        val userId = jwtAuthenticationProvider.getCurrentUserId()

        return if (permissionsManager.hasPermission(userId, serviceId, PermissionType.EDIT)) {
            specificationPersistor.delete(serviceId, version)
        } else {
            throw PermissionDeniedException(serviceId.id)
        }

    }
}