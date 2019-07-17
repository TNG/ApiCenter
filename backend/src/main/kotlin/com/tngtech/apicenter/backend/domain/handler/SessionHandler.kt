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
        // A new uuid is assigned on every authentication

        return if (userPersistor.exists(searchUser.origin, searchUser.externalId)) {
            // A consistent uuid is returned, ie. first one generated on first external authentication
            userPersistor.findByOrigin(searchUser.origin, searchUser.externalId)
        } else {
            userPersistor.save(searchUser)
            searchUser
        }
    }
}