package com.tngtech.apicenter.backend.connector.rest.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authManager: AuthenticationManager) : BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(
        req: HttpServletRequest,
        res: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = req.getHeader("Authorization")

        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(req, res)
            return
        }

        val user = JWT.require(Algorithm.HMAC512("ApiCenterSecuritySecret".toByteArray()))
            .build()
            .verify(header.replace("Bearer ", ""))
            .subject

        SecurityContextHolder.getContext().authentication =
            if (user != null) JwtAuthenticationToken(user, header) else null
        chain.doFilter(req, res)
    }
}