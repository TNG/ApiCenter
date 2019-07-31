package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.entity.Specification

@org.springframework.stereotype.Service
class RemoteServiceUpdater(
        private val specificationFileDownloader: SpecificationFileDownloader,
        private val specificationDataParser: SpecificationDataParser
) {

    fun synchronize(service: Service): Specification {
        val remoteAddress = service.remoteAddress ?: ""

        val fileContent = specificationFileDownloader.retrieveFile(remoteAddress)
        val content = specificationDataParser.parseFileContent(fileContent)
        val metadata = specificationDataParser.makeSpecificationMetadata(content, service.id.id)

        return Specification(content, metadata)
    }
}
