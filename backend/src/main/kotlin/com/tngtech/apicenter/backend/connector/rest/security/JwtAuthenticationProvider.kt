package com.tngtech.apicenter.backend.connector.rest.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtAuthenticationProvider : AuthenticationProvider {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecuritySecret: String;

    override fun authenticate(authentication: Authentication): Authentication? {
        val token = (authentication as JwtAuthenticationToken).token

        val username = JWT.require(Algorithm.HMAC512(jwtSecuritySecret.toByteArray()))
            .build()
            .verify(token)
            .subject

        return if (username != null) JwtAuthenticationToken(username, token) else null
    }

    fun getCurrentUserId(): String =
            try {
                (SecurityContextHolder.getContext().authentication as JwtAuthenticationToken).username
            } catch (exception: ClassCastException) {
                // Thrown when running integration tests
                "user"
            }

    override fun supports(authentication: Class<*>?) = true
}