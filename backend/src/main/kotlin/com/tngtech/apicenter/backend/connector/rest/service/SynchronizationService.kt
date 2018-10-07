package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.handler.SpecificationHandler
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SynchronizationService constructor(
    private val specificationHandler: SpecificationHandler,
    private val specificationFileService: SpecificationFileService,
    private val specificationDataService: SpecificationDataService
) {

    fun synchronize(specificationId: UUID) {
        val specification = specificationHandler.findOne(specificationId)!!

        val remoteAddress = specification.remoteAddress ?: ""

        val fileContent = specificationFileService.retrieveFile(remoteAddress)
        val content = specificationDataService.parseFileContent(fileContent)
        val versionString = specificationDataService.readVersion(content)

        val versions = if (specification.versions.find { version -> version.version == versionString } != null) {
            specification.versions
        } else {
            specification.versions + Version(UUID.randomUUID(), versionString, content)
        }

        val newSpecification = Specification(
            specification.id,
            specificationDataService.readTitle(content),
            specificationDataService.readDescription(content),
            versions,
            specification.remoteAddress
        )

        specificationHandler.store(newSpecification)
    }
}