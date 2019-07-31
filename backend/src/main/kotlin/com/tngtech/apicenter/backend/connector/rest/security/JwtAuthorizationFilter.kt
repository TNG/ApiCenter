package com.tngtech.apicenter.backend.connector.rest.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.SignatureVerificationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(authManager: AuthenticationManager, private val jwtSecuritySecret: String) : BasicAuthenticationFilter(authManager) {

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

        try {
            val username = JWT.require(Algorithm.HMAC512(jwtSecuritySecret.toByteArray()))
                    .build()
                    .verify(header.replace("Bearer ", ""))
                    .subject

            SecurityContextHolder.getContext().authentication =
                    if (username != null) JwtAuthenticationToken(username, header) else null
        } catch (exception: SignatureVerificationException) {
            // Token's signature invalid when verified using server-side secret and HMAC512"
        }
        chain.doFilter(req, res)
    }
}