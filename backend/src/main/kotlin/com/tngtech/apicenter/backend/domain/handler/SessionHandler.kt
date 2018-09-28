package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService
import org.springframework.stereotype.Component

@Component
class SessionHandler constructor(
    private val userHandler: UserHandler,
    private val externalAuthenticationService: ExternalAuthenticationService
) {

    fun authenticate(username: String, password: String): User? {
        val searchUser = externalAuthenticationService.authenticate(username, password) ?: return null

        val storedUser = if (userHandler.checkExistenceByOrigin(searchUser)) {
            userHandler.findByOrigin(searchUser)
        } else {
            searchUser
        }

        val userToStore = if (storedUser.email != searchUser.email || storedUser.username != searchUser.username) {
            User(storedUser.id, searchUser.email, searchUser.username, "crowd", searchUser.externalId)
        } else {
            storedUser
        }

        userHandler.store(userToStore)

        return userToStore
    }
}