package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.User

interface ExternalAuthenticator {
    fun authenticate(username: String, password: String): User?
}