package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VersionRepository : CrudRepository<VersionEntity, VersionId>