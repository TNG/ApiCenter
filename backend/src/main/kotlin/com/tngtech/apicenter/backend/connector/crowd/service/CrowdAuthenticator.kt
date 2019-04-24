package com.tngtech.apicenter.backend.connector.crowd.service

import com.atlassian.crowd.service.client.CrowdClient
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@ConditionalOnProperty(name = ["auth.service"], havingValue = "crowd")
class CrowdAuthenticator @Autowired constructor(private val crowdClient: CrowdClient) : ExternalAuthenticator {

    override fun authenticate(username: String, password: String): User? {
        try {
            val user = crowdClient.authenticateUser(username, password)
            return User(UUID.randomUUID(), user.name, user.emailAddress, "crowd", user.externalId)
        } catch (exception: Exception) {
            return null
        }
    }
}