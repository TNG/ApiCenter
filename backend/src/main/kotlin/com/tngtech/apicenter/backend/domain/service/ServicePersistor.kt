package com.tngtech.apicenter.backend.domain.service

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ServicePersistor {
    fun save(service: Service)
    fun findAll(pageable: Pageable): Page<Service>
    fun findOne(id: ServiceId): Service?
    fun delete(id: ServiceId)
    fun exists(id: ServiceId): Boolean
    fun search(searchString: String): List<Service>
}