package com.tngtech.apicenter.repository

import com.tngtech.apicenter.entity.ApplicationEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ApplicationRepository : CrudRepository<ApplicationEntity, UUID>
