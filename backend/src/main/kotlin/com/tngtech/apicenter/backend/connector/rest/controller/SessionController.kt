package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.LoginDto
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
    fun login(@RequestBody loginDto: LoginDto): Authentication {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginDto.username,
                loginDto.password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication

        return authentication
    }
}