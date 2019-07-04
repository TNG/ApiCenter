package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId

interface PermissionsManager {
    fun addPermission(userId: String, resource: ServiceId, permission: PermissionType)
    fun removePermission(userId: String, resource: ServiceId, permission: PermissionType)
    fun hasPermission(userId: String, resource: ServiceId, permission: PermissionType): Boolean
    fun clearPermissions(userId: String, resource: ServiceId)
}