package com.tngtech.apicenter.backend.connector.acl.service

import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.service.PermissionsManager

@org.springframework.stereotype.Service
class PermissionsService: PermissionsManager {
    override fun addPermission(userId: String, resource: ServiceId, permission: PermissionType) {
    }

    override fun removePermission(userId: String, resource: ServiceId, permission: PermissionType) {
    }

    override fun hasPermission(userId: String, resource: ServiceId, permission: PermissionType): Boolean {
        return true
    }

    override fun clearPermissions(userId: String, resource: ServiceId) {
    }
}