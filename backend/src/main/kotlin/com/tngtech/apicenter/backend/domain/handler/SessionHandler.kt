package com.tngtech.apicenter.backend.domain.handler

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticator
import com.tngtech.apicenter.backend.domain.service.UserPersistor
import org.springframework.stereotype.Component

@Component
class SessionHandler(
        private val userPersistor: UserPersistor,
        private val externalAuthenticator: ExternalAuthenticator
) {

    fun authenticate(username: String, password: String): User? {
        val searchUser = externalAuthenticator.authenticate(username, password) ?: return null

        return if (userPersistor.existsById(searchUser.username)) {
            userPersistor.findById(searchUser.username)
        } else {
            userPersistor.save(searchUser)
            searchUser
        }
    }
}