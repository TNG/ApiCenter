package com.tngtech.apicenter.backend.connector.database.service

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationId
import com.tngtech.apicenter.backend.connector.database.mapper.SpecificationEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistence
import org.springframework.stereotype.Service

@Service
class SpecificationDatabase constructor(
        private val specificationRepository: SpecificationRepository,
        private val specificationEntityMapper: SpecificationEntityMapper
) : SpecificationPersistence {

    override fun findOne(serviceId: ServiceId, version: String): Specification? {
        return specificationRepository.findById(SpecificationId(serviceId.id, version)).orElse(null)?.let { entity -> specificationEntityMapper.toDomain(entity) }
    }


    override fun delete(serviceId: ServiceId, version: String) {
        specificationRepository.deleteById(SpecificationId(serviceId.id, version))
    }
}