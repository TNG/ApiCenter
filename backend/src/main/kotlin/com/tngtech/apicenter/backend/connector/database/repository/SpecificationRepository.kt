package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SpecificationRepository : CrudRepository<SpecificationEntity, String> {

    override fun existsById(id: String): Boolean
    override fun findById(id: String): Optional<SpecificationEntity>
}