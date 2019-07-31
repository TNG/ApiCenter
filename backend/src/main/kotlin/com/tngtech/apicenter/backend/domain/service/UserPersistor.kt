package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.User

interface UserPersistor {
    fun save(user: User)
    fun findById(username: String): User?
    fun existsById(username: String): Boolean
}