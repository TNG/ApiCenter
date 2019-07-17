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

        val userId = JWT.require(Algorithm.HMAC512(jwtSecuritySecret.toByteArray()))
            .build()
            .verify(token)
            .subject

        return if (userId != null) JwtAuthenticationToken(UUID.fromString(userId), token) else null
    }

    fun getCurrentUserId(): UUID =
            try {
                (SecurityContextHolder.getContext().authentication as JwtAuthenticationToken).userId
            } catch (exception: ClassCastException) {
                // Thrown when running integration tests
                UUID.randomUUID()
            }

    override fun supports(authentication: Class<*>?) = true
}