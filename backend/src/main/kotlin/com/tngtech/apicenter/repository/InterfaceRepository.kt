package com.tngtech.apicenter.repository

import com.tngtech.apicenter.entity.InterfaceEntity
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface InterfaceRepository : CrudRepository<InterfaceEntity, UUID> {
    fun findByApplicationId(applicationId: UUID): List<InterfaceEntity>
}
