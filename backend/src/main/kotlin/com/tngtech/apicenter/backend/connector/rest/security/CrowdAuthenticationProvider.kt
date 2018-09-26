package com.tngtech.apicenter.backend.connector.rest.security

import com.atlassian.crowd.service.client.CrowdClient
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.handler.UserHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import java.util.UUID

class CrowdAuthenticationProvider @Autowired constructor(
    private val crowdClient: CrowdClient,
    private val userHandler: UserHandler
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        try {
            val user = crowdClient.authenticateUser(authentication.name, authentication.credentials.toString())
            val searchUser = User(UUID.randomUUID(), user.name, user.emailAddress, "crowd", user.externalId)

            val storedUser = if (!userHandler.checkExistenceByOrigin(searchUser)) {
                userHandler.findByOrigin(searchUser)
            } else {
                searchUser
            }

            val userToStore = if (storedUser.email != user.emailAddress || storedUser.username != user.name) {
                User(storedUser.id, user.emailAddress, user.name, "crowd", user.externalId)
            } else {
                storedUser
            }

            userHandler.store(userToStore)

            return UsernamePasswordAuthenticationToken(user, authentication.credentials)
        } catch (exception: Exception) {
            return null;
        }
    }

    override fun supports(authentication: Class<*>) = true
}