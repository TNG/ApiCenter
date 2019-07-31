package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.ResultPage
import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.entity.ServiceId

interface ServicePersistor {
    fun save(service: Service)
    fun findAllOrderByTitle(pageNumber: Int, pageSize: Int, username: String, anonymousUsername: String): ResultPage<Service>
    fun findOne(id: ServiceId): Service?
    fun delete(id: ServiceId)
    fun exists(id: ServiceId): Boolean
    fun search(searchString: String): List<Service>
}
