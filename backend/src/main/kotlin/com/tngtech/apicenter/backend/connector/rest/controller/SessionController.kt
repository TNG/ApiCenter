package com.tngtech.apicenter.backend.connector.rest.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tngtech.apicenter.backend.connector.rest.dto.LoginDto
import com.tngtech.apicenter.backend.connector.rest.dto.SessionDto
import com.tngtech.apicenter.backend.domain.handler.SessionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/sessions")
class SessionController(private val sessionHandler: SessionHandler) {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecuritySecret: String

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<SessionDto> {
        val user = sessionHandler.authenticate(loginDto.username, loginDto.password)
            ?: return ResponseEntity(HttpStatus.UNAUTHORIZED)

        val jwt = JWT.create()
            .withSubject(user.username)
            .sign(Algorithm.HMAC512(jwtSecuritySecret.toByteArray()))

        return ResponseEntity.ok(SessionDto("Bearer $jwt", user.username))
    }
}