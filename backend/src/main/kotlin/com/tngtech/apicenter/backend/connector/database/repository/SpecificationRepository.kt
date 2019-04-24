package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SpecificationRepository : CrudRepository<SpecificationEntity, SpecificationId>