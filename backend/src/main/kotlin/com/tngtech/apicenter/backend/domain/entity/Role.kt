package com.tngtech.apicenter.backend.domain.entity

import java.util.*

enum class Role {
    VIEWER {
        override fun permissions(): EnumSet<PermissionType> =
                EnumSet.of(PermissionType.VIEW)
    },

    VIEWER_X {
        override fun permissions(): EnumSet<PermissionType> =
                EnumSet.of(PermissionType.VIEW, PermissionType.VIEWPRERELEASE)
    },

    EDITOR {
        override fun permissions(): EnumSet<PermissionType> =
                EnumSet.of(PermissionType.VIEW, PermissionType.VIEWPRERELEASE, PermissionType.EDIT)
    };

    abstract fun permissions(): EnumSet<PermissionType>
}