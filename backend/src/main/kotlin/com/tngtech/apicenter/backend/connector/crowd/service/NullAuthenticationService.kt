package com.tngtech.apicenter.backend.connector.crowd.service

import com.atlassian.crowd.service.client.CrowdClient
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.UUID

class NullAuthenticationService : ExternalAuthenticationService {

    override fun authenticate(username: String, password: String): User? {
        try {
            return User(UUID.randomUUID(), username, "", "null", "null")
        } catch (exception: Exception) {
            return null
        }
    }
}