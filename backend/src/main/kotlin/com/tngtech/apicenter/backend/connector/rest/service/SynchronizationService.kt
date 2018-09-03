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

        val newSpecification = Specification(
            specification.id,
            specificationDataService.readTitle(content),
            specificationDataService.readDescription(content),
            Version(specificationDataService.readVersion(content)),
            content,
            specification.remoteAddress
        )

        specificationHandler.store(newSpecification)
    }
}