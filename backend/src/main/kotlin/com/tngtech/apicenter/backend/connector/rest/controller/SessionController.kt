package com.tngtech.apicenter.backend.connector.rest.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tngtech.apicenter.backend.connector.rest.dto.LoginDto
import com.tngtech.apicenter.backend.connector.rest.dto.SessionDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sessions")
class SessionController @Autowired constructor(private val authenticationManager: AuthenticationManager) {

    @PostMapping
    fun login(@RequestBody loginDto: LoginDto): SessionDto {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.username,
                loginDto.password
            )
        )

        val jwt = JWT.create()
            .withSubject(authentication.name)
            .sign(Algorithm.HMAC512("ApiCenterSecuritySecret".toByteArray()))

        return SessionDto("Bearer $jwt")
    }
}