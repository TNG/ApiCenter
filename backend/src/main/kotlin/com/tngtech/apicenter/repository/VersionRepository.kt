package com.tngtech.apicenter.repository

import com.tngtech.apicenter.entity.VersionEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface VersionRepository : CrudRepository<VersionEntity, UUID>
