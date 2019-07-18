package com.tngtech.apicenter.backend.connector.rest.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import java.util.*

data class JwtAuthenticationToken(val username: String, val token: String) : AbstractAuthenticationToken(AuthorityUtils.NO_AUTHORITIES) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials() = "N/A"

    override fun getPrincipal() = null
}