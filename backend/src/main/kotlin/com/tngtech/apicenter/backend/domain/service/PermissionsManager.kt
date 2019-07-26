package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.Role
import com.tngtech.apicenter.backend.domain.entity.ServiceId

interface PermissionsManager {
    fun assignRole(username: String, serviceId: ServiceId, role: Role)
    fun removeRole(username: String, serviceId: ServiceId)
    fun hasPermission(username: String, serviceId: ServiceId, permission: PermissionType): Boolean
    fun getRole(username: String, serviceId: ServiceId): Role?
}