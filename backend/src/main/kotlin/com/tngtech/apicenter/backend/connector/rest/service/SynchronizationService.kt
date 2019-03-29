package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.springframework.stereotype.Service

@Service
class SynchronizationService constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val specificationFileService: SpecificationFileService,
    private val specificationDataService: SpecificationDataService
) {

    fun synchronize(specificationId: ServiceId) {
        val specification = specificationPersistenceService.findOne(specificationId)!!

        val remoteAddress = specification.remoteAddress ?: ""

        val fileContent = specificationFileService.retrieveFile(remoteAddress)
        val content = specificationDataService.parseFileContent(fileContent)
        val versionString = specificationDataService.extractVersion(content)

        val versions = if (specification.versions.find { version -> version.metadata.version == versionString } != null) {
            specification.versions
        } else {
            specification.versions + Version(content,
                    VersionMetaData(specificationId, specification.title, versionString, specification.description, ApiLanguage.OPENAPI))
        }

        val newSpecification = Specification(
            specification.id,
            specificationDataService.extractTitle(content),
            specificationDataService.extractDescription(content),
            versions,
            specification.remoteAddress
        )

        specificationPersistenceService.save(newSpecification)
    }
}