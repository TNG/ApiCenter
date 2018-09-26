package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.User

interface UserPersistenceService {
    fun save(user: User)
    fun exists(origin: String, externalId: String): Boolean
}