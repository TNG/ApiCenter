package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification

interface SpecificationPersistence {
    fun findOne(serviceId: ServiceId, version: String): Specification?
    fun delete(serviceId: ServiceId, version: String)
}