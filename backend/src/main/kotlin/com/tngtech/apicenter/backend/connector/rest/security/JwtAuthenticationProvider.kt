package com.tngtech.apicenter.backend.connector.rest.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication? {
        val token = (authentication as JwtAuthenticationToken).token

        val user = JWT.require(Algorithm.HMAC512("ApiCenterSecuritySecret".toByteArray()))
            .build()
            .verify(token)
            .subject

        return if (user != null) JwtAuthenticationToken(user, token) else null
    }

    override fun supports(authentication: Class<*>?) = true
}