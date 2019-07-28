package com.tngtech.apicenter.backend.connector.database.mapper

import com.tngtech.apicenter.backend.connector.database.entity.UserEntity
import com.tngtech.apicenter.backend.domain.entity.User
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Component

@Component
class UserEntityMapper(private val mapperFacade: MapperFacade) {

    fun toDomain(userEntity: UserEntity): User = mapperFacade.map(userEntity, User::class.java)

    fun fromDomain(user: User): UserEntity = mapperFacade.map(user, UserEntity::class.java)

}