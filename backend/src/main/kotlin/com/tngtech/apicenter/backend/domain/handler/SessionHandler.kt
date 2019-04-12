package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthentication
import com.tngtech.apicenter.backend.domain.service.UserPersistence
import org.springframework.stereotype.Component

@Component
class SessionHandler constructor(
        private val userPersistence: UserPersistence,
        private val externalAuthentication: ExternalAuthentication
) {

    fun authenticate(username: String, password: String): User? {
        val searchUser = externalAuthentication.authenticate(username, password) ?: return null

        val storedUser = if (userPersistence.exists(searchUser.origin, searchUser.externalId)) {
            userPersistence.findByOrigin(searchUser.origin, searchUser.externalId)
        } else {
            searchUser
        }

        val userToStore = if (storedUser.email != searchUser.email || storedUser.username != searchUser.username) {
            User(storedUser.id, searchUser.email, searchUser.username, "crowd", searchUser.externalId)
        } else {
            storedUser
        }

        if (userPersistence.exists(searchUser.origin, searchUser.externalId)) {
            userPersistence.save(userToStore)
        }

        return userToStore
    }
}