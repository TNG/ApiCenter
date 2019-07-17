package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import java.util.*

interface PermissionsManager {
    fun addPermission(userId: UUID, serviceId: ServiceId, permission: PermissionType)
    fun removePermission(userId: UUID, serviceId: ServiceId, permission: PermissionType)
    fun hasPermission(userId: UUID, serviceId: ServiceId, permission: PermissionType): Boolean
    fun clearPermissions(userId: UUID, serviceId: ServiceId)
}