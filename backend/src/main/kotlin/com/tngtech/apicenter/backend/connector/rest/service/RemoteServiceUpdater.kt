package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.handler.ServiceHandler
import org.springframework.stereotype.Service

@Service
class RemoteServiceUpdater constructor(
        private val serviceHandler: ServiceHandler,
        private val specificationFileDownloader: SpecificationFileDownloader,
        private val specificationDataParser: SpecificationDataParser
) {

    fun synchronize(serviceId: ServiceId) {
        val service = serviceHandler.findOne(serviceId)

        service ?: throw SpecificationNotFoundException(serviceId.id)

        val remoteAddress = service.remoteAddress ?: ""

        val fileContent = specificationFileDownloader.retrieveFile(remoteAddress)
        val content = specificationDataParser.parseFileContent(fileContent)
        val metadata = specificationDataParser.makeSpecificationMetadata(content, serviceId.id)

        val specification = Specification(content, metadata)
        serviceHandler.addNewSpecification(specification, serviceId, service.remoteAddress)
    }
}