package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.UserEntity
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : CrudRepository<UserEntity, UUID> {

    @Query("select count(u)>0 from UserEntity u where u.origin = :origin and u.externalId = :externalId")
    fun checkExistenceByOrigin(@Param("origin") origin: String, @Param("externalId") externalId: String): Boolean
}