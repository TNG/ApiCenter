package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.ServiceEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ServiceRepository : PagingAndSortingRepository<ServiceEntity, String> {

    override fun existsById(id: String): Boolean
    override fun findById(id: String): Optional<ServiceEntity>
}