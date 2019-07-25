package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServiceRepository : PagingAndSortingRepository<ServiceEntity, String> {

    override fun existsById(id: String): Boolean
    override fun findById(id: String): Optional<ServiceEntity>

    @Query("select s from ServiceEntity s, AccessRecordEntity a where a.accessRecordId.serviceId = s.id and (a.accessRecordId.username = :username or a.accessRecordId.username = :anonymousUsername)")
    fun findAllUsersWithPagination(pageable: Pageable, @Param("username") username: String, @Param("anonymousUsername") anonymousUsername: String): Page<ServiceEntity>
}