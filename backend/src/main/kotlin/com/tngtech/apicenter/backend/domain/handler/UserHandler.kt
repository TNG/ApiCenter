package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.UserPersistenceService

class UserHandler constructor(private val userPersistenceService: UserPersistenceService) {

    fun store(user: User) {
        userPersistenceService.save(user)
    }

    fun findByOrigin(user: User) = userPersistenceService.findByOrigin(user.origin, user.externalId)

    fun checkExistenceByOrigin(user: User) = userPersistenceService.exists(user.origin, user.externalId)
}