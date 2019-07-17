package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordEntity
import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordId
import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.connector.database.repository.AccessRecordRepository
import com.tngtech.apicenter.backend.connector.database.repository.ServiceRepository
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import java.util.*

@org.springframework.stereotype.Service
class AccessRecordDatabase constructor(
        private val accessRecordRepository: AccessRecordRepository,
        private val serviceRepository: ServiceRepository
): PermissionsManager {
    override fun addPermission(userId: UUID, serviceId: ServiceId, permission: PermissionType) {
        val key = AccessRecordId(serviceId.id, userId)
        val service = serviceRepository.findById(serviceId.id)
        service.ifPresent { serviceEntity ->
            val record = accessRecordRepository.findById(key)

            val view: Boolean = (permission == PermissionType.VIEW) ||
                    record.map { entity -> entity.view }.orElse(false)

            val viewPrereleases: Boolean = (permission == PermissionType.VIEWPRERELEASE) ||
                    record.map { entity -> entity.viewPrereleases }.orElse(false)

            val edit: Boolean = (permission == PermissionType.EDIT) ||
                    record.map { entity -> entity.edit }.orElse(false)

            accessRecordRepository.save(AccessRecordEntity(key, serviceEntity, view, viewPrereleases, edit))
        }
    }

    override fun removePermission(userId: UUID, serviceId: ServiceId, permission: PermissionType) {
        val key = AccessRecordId(serviceId.id, userId)
        val record = accessRecordRepository.findById(key)

        record.ifPresent { entity ->
            val view = if (permission == PermissionType.VIEW) false else entity.view
            val viewPrereleases = if (permission == PermissionType.VIEWPRERELEASE) false else entity.viewPrereleases
            val edit = if (permission == PermissionType.EDIT) false else entity.edit
            accessRecordRepository.save(AccessRecordEntity(key, entity.serviceEntity, view, viewPrereleases, edit))
        }
    }

    override fun hasPermission(userId: UUID, serviceId: ServiceId, permission: PermissionType): Boolean {
        val key = AccessRecordId(serviceId.id, userId)
        val record = accessRecordRepository.findById(key)
        return record.map { entity ->
            when (permission) {
                PermissionType.VIEW -> entity.view
                PermissionType.VIEWPRERELEASE -> entity.viewPrereleases
                PermissionType.EDIT -> entity.edit
            }
        }.orElse(false)
    }

    override fun clearPermissions(userId: UUID, serviceId: ServiceId) {
        val key = AccessRecordId(serviceId.id, userId)
        accessRecordRepository.deleteById(key)
    }
}