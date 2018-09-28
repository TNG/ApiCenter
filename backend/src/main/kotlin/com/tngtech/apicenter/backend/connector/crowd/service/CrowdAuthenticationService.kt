package com.tngtech.apicenter.backend.connector.crowd.service

import com.atlassian.crowd.service.client.CrowdClient
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CrowdAuthenticationService @Autowired constructor(private val crowdClient: CrowdClient) : ExternalAuthenticationService {

    override fun authenticate(username: String, password: String): User? {
        try {
            val user = crowdClient.authenticateUser(username, password)
            return User(UUID.randomUUID(), user.name, user.emailAddress, "crowd", user.externalId)
        } catch (exception: Exception) {
            return null
        }
    }
}