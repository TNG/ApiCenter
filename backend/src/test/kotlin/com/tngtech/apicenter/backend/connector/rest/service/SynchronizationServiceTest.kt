package com.tngtech.apicenter.backend.connector.rest.service

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.junit.Test

class SynchronizationServiceTest {

    private val specificationPersistenceService: SpecificationPersistenceService = mock()

    private val specificationFileService: SpecificationFileService = mock()

    private val specificationDataService: SpecificationDataService = mock()

    private val synchronizationService =
        SynchronizationService(specificationPersistenceService, specificationFileService, specificationDataService)

    companion object {
        const val SPECIFICATION_ID = "ff9da045-05f7-4f3d-9801-da609086935c"
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\", \"description\": \"Description\"}}"
        const val UPDATED_SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"2.0.0\",\"title\": \"Swagger Petstore 2\", \"description\": \"Updated Description\"}}"
        const val REMOTE_ADDRESS = "http://testapi.com/testapi.json"
    }

    private val metadata = VersionMetaData(ServiceId(SPECIFICATION_ID), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    @Test
    fun synchronize_shouldStoreAdaptedSpecification() {
        val id = ServiceId(SPECIFICATION_ID)

        val specification = Specification(
            id,
            "Swagger Petstore",
            "Description",
            listOf(Version(SWAGGER_SPECIFICATION, metadata)),
            REMOTE_ADDRESS
        )

        given(specificationPersistenceService.findOne(ServiceId(SPECIFICATION_ID))).willReturn(specification)
        given(specificationFileService.retrieveFile(REMOTE_ADDRESS)).willReturn(UPDATED_SWAGGER_SPECIFICATION)
        given(specificationDataService.parseFileContent(UPDATED_SWAGGER_SPECIFICATION)).willReturn(
            UPDATED_SWAGGER_SPECIFICATION
        )
        given(specificationDataService.makeSpecificationMetaData(UPDATED_SWAGGER_SPECIFICATION, id, REMOTE_ADDRESS)).willReturn(metadata)

        synchronizationService.synchronize(ServiceId(SPECIFICATION_ID))

        verify(specificationPersistenceService).saveOne(
                Version(UPDATED_SWAGGER_SPECIFICATION, metadata),
                id,
                REMOTE_ADDRESS)
    }
}