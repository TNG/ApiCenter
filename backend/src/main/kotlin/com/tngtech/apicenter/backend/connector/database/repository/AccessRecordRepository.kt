package com.tngtech.apicenter.backend.connector.database.repository

import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordEntity
import com.tngtech.apicenter.backend.connector.database.entity.AccessRecordId
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AccessRecordRepository: CrudRepository<AccessRecordEntity, AccessRecordId> {
    @Query("select count(a)>0 from AccessRecordEntity a where a.role = 2 and a.accessRecordId.serviceId = :serviceId and a.accessRecordId.username <> :username")
    fun otherEditorsExist(@Param("serviceId") serviceId: String,
                          @Param("username") userToDowngrade: String): Boolean

    @Modifying
    @Query("delete from AccessRecordEntity a where a.accessRecordId.serviceId = :serviceId")
    fun clearPermissions(@Param("serviceId") serviceId: String)
}
