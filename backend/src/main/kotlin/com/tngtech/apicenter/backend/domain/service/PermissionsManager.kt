package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import java.util.*

interface PermissionsManager {
    fun addPermission(username: String, serviceId: ServiceId, permission: PermissionType)
    fun removePermission(username: String, serviceId: ServiceId, permission: PermissionType)
    fun hasPermission(username: String, serviceId: ServiceId, permission: PermissionType): Boolean
    fun clearPermissions(serviceId: ServiceId)
}