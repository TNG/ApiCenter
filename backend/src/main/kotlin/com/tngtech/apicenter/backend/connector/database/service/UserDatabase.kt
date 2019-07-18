package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.UserEntity
import com.tngtech.apicenter.backend.connector.database.mapper.UserEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.UserRepository
import com.tngtech.apicenter.backend.domain.entity.User
import com.tngtech.apicenter.backend.domain.service.UserPersistor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class UserDatabase constructor(
    private val userRepository: UserRepository,
    private val userEntityMapper: UserEntityMapper
) : UserPersistor {

    override fun save(user: User) {
        userRepository.save(userEntityMapper.fromDomain(user))
    }

    override fun findById(username: String): User? =
            userRepository.findById(username).orElse(null)?.let { spec -> userEntityMapper.toDomain(spec) }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    override fun existsById(username: String) = userRepository.existsByUsername(username)
}