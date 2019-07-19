package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordEntity
import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordId
import com.tngtech.apicenter.backend.connector.database.entity.UserEntity
import com.tngtech.apicenter.backend.connector.database.repository.AccessRecordRepository
import com.tngtech.apicenter.backend.connector.database.repository.ServiceRepository
import com.tngtech.apicenter.backend.connector.database.repository.UserRepository
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.NotEnoughEditorsException
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import org.springframework.transaction.annotation.Transactional

@org.springframework.stereotype.Service
class AccessRecordDatabase constructor(
        private val accessRecordRepository: AccessRecordRepository,
        private val serviceRepository: ServiceRepository,
        private val userRepository: UserRepository
): PermissionsManager {
    override fun addPermission(username: String, serviceId: ServiceId, permission: PermissionType) {
        val key = AccessRecordId(serviceId.id, username)
        val service = serviceRepository.findById(serviceId.id)
        val user = userRepository.findById(username)

        service.ifPresent { serviceEntity ->

            user.ifPresent { userEntity: UserEntity ->

                val record = accessRecordRepository.findById(key)

                val view: Boolean = (permission == PermissionType.VIEW) ||
                        record.map { entity -> entity.view }.orElse(false)

                val viewPrereleases: Boolean = (permission == PermissionType.VIEWPRERELEASE) ||
                        record.map { entity -> entity.viewPrereleases }.orElse(false)

                val edit: Boolean = (permission == PermissionType.EDIT) ||
                        record.map { entity -> entity.edit }.orElse(false)

                accessRecordRepository.save(AccessRecordEntity(key, serviceEntity, userEntity, view, viewPrereleases, edit))
            }
        }
    }

    override fun removePermission(username: String, serviceId: ServiceId, permission: PermissionType) {
        val key = AccessRecordId(serviceId.id, username)
        val record = accessRecordRepository.findById(key)

        record.ifPresent { entity ->
            val view = if (permission == PermissionType.VIEW) false else entity.view
            val viewPrereleases = if (permission == PermissionType.VIEWPRERELEASE) false else entity.viewPrereleases
            val edit = if (permission == PermissionType.EDIT) false else entity.edit

            if (edit && !accessRecordRepository.otherEditorsExist(serviceId.id, username)) {
                throw NotEnoughEditorsException(serviceId.id)
            }
            accessRecordRepository.save(AccessRecordEntity(key, entity.serviceEntity, entity.userEntity, view, viewPrereleases, edit))
        }
    }

    override fun hasPermission(username: String, serviceId: ServiceId, permission: PermissionType): Boolean {
        val key = AccessRecordId(serviceId.id, username)
        val record = accessRecordRepository.findById(key)
        return record.map { entity ->
            when (permission) {
                PermissionType.VIEW -> entity.view
                PermissionType.VIEWPRERELEASE -> entity.viewPrereleases
                PermissionType.EDIT -> entity.edit
            }
        }.orElse(false)
    }

    @Transactional
    override fun clearPermissions(serviceId: ServiceId) {
        accessRecordRepository.clearPermissions(serviceId.id)
    }
}