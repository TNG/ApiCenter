package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordEntity
import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordId
import com.tngtech.apicenter.backend.connector.database.entity.UserEntity
import com.tngtech.apicenter.backend.connector.database.repository.AccessRecordRepository
import com.tngtech.apicenter.backend.connector.database.repository.ServiceRepository
import com.tngtech.apicenter.backend.connector.database.repository.UserRepository
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.Role
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.NotEnoughEditorsException
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import java.util.*

@org.springframework.stereotype.Service
class AccessRecordDatabase constructor(
        private val accessRecordRepository: AccessRecordRepository,
        private val serviceRepository: ServiceRepository,
        private val userRepository: UserRepository
): PermissionsManager {
    override fun assignRole(username: String, serviceId: ServiceId, role: Role) {
        val key = AccessRecordId(serviceId.id, username)
        val service = serviceRepository.findById(serviceId.id)
        val user = userRepository.findById(username)

        service.ifPresent { serviceEntity ->

            user.ifPresent { userEntity: UserEntity ->

                val record = accessRecordRepository.findById(key)

                record.ifPresent { entity: AccessRecordEntity ->
                    val isEditor = entity.role == Role.EDITOR
                    val willStayEditor = role == Role.EDITOR
                    if (isEditor && !willStayEditor && !accessRecordRepository.otherEditorsExist(serviceId.id, username)) {
                        throw NotEnoughEditorsException(serviceId.id)
                    }
                }

                accessRecordRepository.save(AccessRecordEntity(key, serviceEntity, userEntity, role))
            }
        }
    }

    override fun removeRole(username: String, serviceId: ServiceId) {
        val key = AccessRecordId(serviceId.id, username)
        val record = accessRecordRepository.findById(key)

        record.ifPresent { entity ->
            val isEditor = entity.role == Role.EDITOR
            if (isEditor && !accessRecordRepository.otherEditorsExist(serviceId.id, username)) {
                throw NotEnoughEditorsException(serviceId.id)
            }
            accessRecordRepository.deleteById(entity.accessRecordId)
        }
    }

    override fun hasPermission(username: String, serviceId: ServiceId, permission: PermissionType): Boolean {
        val key = AccessRecordId(serviceId.id, username)
        val record = accessRecordRepository.findById(key)
        return record.map { entity ->
            checkRole(permission, entity.role)
        }.orElse(false)
    }

    override fun getRole(username: String, serviceId: ServiceId): Role? {
        val key = AccessRecordId(serviceId.id, username)
        val record = accessRecordRepository.findById(key)
        return record.map { entity: AccessRecordEntity -> entity.role }.orElse(null)
    }

    private fun checkRole(permissionType: PermissionType, role: Role): Boolean {
        val viewer = EnumSet.of(PermissionType.VIEW)
        val viewerX = EnumSet.of(PermissionType.VIEW, PermissionType.VIEWPRERELEASE)
        val editor = EnumSet.of(PermissionType.VIEW, PermissionType.VIEWPRERELEASE, PermissionType.EDIT)

        return when (role) {
            Role.VIEWER -> viewer.contains(permissionType)
            Role.VIEWER_X -> viewerX.contains(permissionType)
            Role.EDITOR -> editor.contains(permissionType)
        }
    }
}