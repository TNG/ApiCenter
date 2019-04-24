package com.tngtech.apicenter.backend.connector.rest.service

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileMetadata
import com.tngtech.apicenter.backend.domain.entity.*
import com.tngtech.apicenter.backend.domain.handler.ServiceHandler
import org.junit.Test

class RemoteServiceUpdaterTest {

    private val serviceHandler: ServiceHandler = mock()

    private val specificationFileDownloader: SpecificationFileDownloader = mock()

    private val specificationDataParser: SpecificationDataParser = mock()

    private val remoteServiceUpdater =
        RemoteServiceUpdater(serviceHandler, specificationFileDownloader, specificationDataParser)

    companion object {
        const val SPECIFICATION_ID = "ff9da045-05f7-4f3d-9801-da609086935c"
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\", \"description\": \"Description\"}}"
        const val UPDATED_SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"2.0.0\",\"title\": \"Swagger Petstore 2\", \"description\": \"Updated Description\"}}"
        const val REMOTE_ADDRESS = "http://testapi.com/testapi.json"
    }

    private val metadata = SpecificationMetadata(ServiceId(SPECIFICATION_ID), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    @Test
    fun synchronize_shouldStoreUpdatedService() {
        val id = ServiceId(SPECIFICATION_ID)

        val service = Service(
            id,
            "Swagger Petstore",
            "Description",
            listOf(Specification(SWAGGER_SPECIFICATION, metadata)),
            REMOTE_ADDRESS
        )

        given(serviceHandler.findOne(ServiceId(SPECIFICATION_ID))).willReturn(service)
        given(specificationFileDownloader.retrieveFile(REMOTE_ADDRESS)).willReturn(UPDATED_SWAGGER_SPECIFICATION)
        given(specificationDataParser.parseFileContent(UPDATED_SWAGGER_SPECIFICATION)).willReturn(
            UPDATED_SWAGGER_SPECIFICATION
        )
        given(specificationDataParser.makeSpecificationMetadata(
                fileContent = UPDATED_SWAGGER_SPECIFICATION,
                idFromPath = SPECIFICATION_ID
        )).willReturn(metadata)

        remoteServiceUpdater.synchronize(ServiceId(SPECIFICATION_ID))

        verify(serviceHandler).addNewSpecification(
                Specification(UPDATED_SWAGGER_SPECIFICATION, metadata),
                id,
                REMOTE_ADDRESS)
    }
}