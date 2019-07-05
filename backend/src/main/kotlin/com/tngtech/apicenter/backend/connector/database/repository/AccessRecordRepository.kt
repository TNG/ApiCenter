package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordEntity
import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordId
import org.springframework.data.repository.CrudRepository

interface AccessRecordRepository: CrudRepository<AccessRecordEntity, AccessRecordId>
