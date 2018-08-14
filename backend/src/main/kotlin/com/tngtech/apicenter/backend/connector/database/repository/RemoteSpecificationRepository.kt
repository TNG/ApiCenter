package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.RemoteSpecificationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RemoteSpecificationRepository : CrudRepository<RemoteSpecificationEntity, UUID>