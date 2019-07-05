package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId

interface PermissionsManager {
    fun addPermission(userId: String, serviceId: ServiceId, permission: PermissionType)
    fun removePermission(userId: String, serviceId: ServiceId, permission: PermissionType)
    fun hasPermission(userId: String, serviceId: ServiceId, permission: PermissionType): Boolean
    fun clearPermissions(userId: String, serviceId: ServiceId)
}