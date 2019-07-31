package com.tngtech.apicenter.backend.connector.crowd.service

import com.atlassian.crowd.service.client.CrowdClient
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(name = ["auth.service"], havingValue = "crowd")
class CrowdAuthenticator(private val crowdClient: CrowdClient) : ExternalAuthenticator {

    override fun authenticate(username: String, password: String): User? =
        try {
            val user = crowdClient.authenticateUser(username, password)
            User(user.name, user.emailAddress)
        } catch (exception: Exception) {
            null
        }
}
