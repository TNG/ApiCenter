package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticator
import com.tngtech.apicenter.backend.domain.service.UserPersistor
import org.springframework.stereotype.Component

@Component
class SessionHandler constructor(
        private val userPersistor: UserPersistor,
        private val externalAuthenticator: ExternalAuthenticator
) {

    fun authenticate(username: String, password: String): User? {
        val searchUser = externalAuthenticator.authenticate(username, password) ?: return null

        val storedUser = if (userPersistor.exists(searchUser.origin, searchUser.externalId)) {
            userPersistor.findByOrigin(searchUser.origin, searchUser.externalId)
        } else {
            searchUser
        }

        val userToStore = if (storedUser.email != searchUser.email || storedUser.username != searchUser.username) {
            User(storedUser.id, searchUser.email, searchUser.username, "crowd", searchUser.externalId)
        } else {
            storedUser
        }

        if (userPersistor.exists(searchUser.origin, searchUser.externalId)) {
            userPersistor.save(userToStore)
        }

        return userToStore
    }
}