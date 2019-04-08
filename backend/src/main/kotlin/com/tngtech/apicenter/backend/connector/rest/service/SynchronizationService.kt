package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.springframework.stereotype.Service

@Service
class SynchronizationService constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val specificationFileService: SpecificationFileService,
    private val specificationDataService: SpecificationDataService
) {

    fun synchronize(specificationId: ServiceId) {
        val specification = specificationPersistenceService.findOne(specificationId)

        specification ?: throw SpecificationNotFoundException(specificationId.id)

        val remoteAddress = specification.remoteAddress ?: ""

        val fileContent = specificationFileService.retrieveFile(remoteAddress)
        val content = specificationDataService.parseFileContent(fileContent)
        val metaData = specificationDataService.makeSpecificationMetaData(content, specificationId, remoteAddress)

        val newVersion = Version(content, metaData)
        specificationPersistenceService.saveOne(newVersion, specificationId, specification.remoteAddress)
    }
}