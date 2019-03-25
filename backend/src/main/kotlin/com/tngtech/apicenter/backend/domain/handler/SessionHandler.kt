package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService
import com.tngtech.apicenter.backend.domain.service.UserPersistenceService
import org.springframework.stereotype.Component

@Component
class SessionHandler constructor(
    private val userPersistenceService: UserPersistenceService,
    private val externalAuthenticationService: ExternalAuthenticationService
) {

    fun authenticate(username: String, password: String): User? {
        val searchUser = externalAuthenticationService.authenticate(username, password) ?: return null

        val storedUser = if (userPersistenceService.exists(searchUser.origin, searchUser.externalId)) {
            userPersistenceService.findByOrigin(searchUser.origin, searchUser.externalId)
        } else {
            searchUser
        }

        val userToStore = if (storedUser.email != searchUser.email || storedUser.username != searchUser.username) {
            User(storedUser.id, searchUser.email, searchUser.username, "crowd", searchUser.externalId)
        } else {
            storedUser
        }

        if (userPersistenceService.exists(searchUser.origin, searchUser.externalId)) {
            userPersistenceService.save(userToStore)
        }

        return userToStore
    }
}