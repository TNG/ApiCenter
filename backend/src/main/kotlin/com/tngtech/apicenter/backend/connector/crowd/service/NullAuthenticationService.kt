package com.tngtech.apicenter.backend.connector.crowd.service

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@ConditionalOnProperty(name = ["auth.service"], havingValue = "none", matchIfMissing=true)
class NullAuthenticationService : ExternalAuthenticationService {

    override fun authenticate(username: String, password: String): User? {
        try {
            return User(UUID.randomUUID(), username, "", "null", "null")
        } catch (exception: Exception) {
            return null
        }
    }
}