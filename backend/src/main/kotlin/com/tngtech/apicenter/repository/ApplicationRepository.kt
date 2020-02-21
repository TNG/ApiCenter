package com.tngtech.apicenter.repository

import com.tngtech.apicenter.entity.ApplicationEntity
import java.util.UUID
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, UUID>
