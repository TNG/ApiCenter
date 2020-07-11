package com.tngtech.apicenter.repository

import com.tngtech.apicenter.entity.VersionEntity
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface VersionRepository : CrudRepository<VersionEntity, UUID>
