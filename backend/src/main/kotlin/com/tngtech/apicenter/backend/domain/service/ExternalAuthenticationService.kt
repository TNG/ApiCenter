package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.User

interface ExternalAuthenticationService {
    fun authenticate(username: String, password: String): User?
}