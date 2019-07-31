package com.tngtech.apicenter.backend.connector.crowd.service

import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticator
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import java.util.UUID

@Service
@ConditionalOnProperty(name = ["auth.service"], havingValue = "none", matchIfMissing=true)
class NullAuthenticator : ExternalAuthenticator {

    override fun authenticate(username: String, password: String): User =
            User(username, "user@example.com")
}