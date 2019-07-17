package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import com.tngtech.apicenter.backend.domain.entity.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServiceRepository : PagingAndSortingRepository<ServiceEntity, String> {

    override fun existsById(id: String): Boolean
    override fun findById(id: String): Optional<ServiceEntity>

    @Query("select s from ServiceEntity s inner join AccessRecordEntity a on a.accessRecordId.serviceId = s.id where a.accessRecordId.userId = :userId")
    fun findAllUsersWithPagination(pageable: Pageable, @Param("userId") userId: UUID): Page<ServiceEntity>
}