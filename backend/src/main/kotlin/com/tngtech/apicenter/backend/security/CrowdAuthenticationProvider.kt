package com.tngtech.apicenter.backend.security

import com.atlassian.crowd.service.client.CrowdClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication

class CrowdAuthenticationProvider @Autowired constructor(private val crowdClient: CrowdClient) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        try {
            val user = crowdClient.authenticateUser(authentication.name, authentication.credentials.toString())

            return UsernamePasswordAuthenticationToken(user, authentication.credentials)
        } catch (exception: Exception) {
            return null;
        }
    }

    override fun supports(authentication: Class<*>) = true
}