package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class VersionFileDtoConverterTest {

    companion object {
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\",\"description\":\"Description\"}}"
        const val SWAGGER_REMOTE = "https://swagger.com/remote/file.json"
        const val UUID_STRING = "5aa40ba9-7e26-44de-81ec-f545d1f178aa"
    }
    private val metadata = VersionMetaData(ServiceId(UUID_STRING), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    private val specificationDataService: SpecificationDataService = mock()

    private val specificationFileService: SpecificationFileService = mock()

    private val versionFileDtoConverter: VersionFileDtoConverter =
        VersionFileDtoConverter(
            specificationFileService,
            specificationDataService
        )

    @Before
    fun setup() {
        given(specificationDataService.extractTitle(SWAGGER_SPECIFICATION)).willReturn("Swagger Petstore")
        given(specificationDataService.extractVersion(SWAGGER_SPECIFICATION)).willReturn("1.0.0")
        given(specificationDataService.extractDescription(SWAGGER_SPECIFICATION)).willReturn("Description")
        given(specificationDataService.parseFileContent(SWAGGER_SPECIFICATION)).willReturn(SWAGGER_SPECIFICATION)
    }

    @Test
    fun convert_shouldReturnSpecification() {
        val versionFileDto = VersionFileDto(SWAGGER_SPECIFICATION, id = UUID_STRING)

        given(specificationFileService.getLocalOrRemoteFileContent(versionFileDto)).willReturn(SWAGGER_SPECIFICATION)
        given(specificationDataService.makeSpecificationMetaData(SWAGGER_SPECIFICATION, ServiceId(UUID_STRING), versionFileDto.fileUrl)).willReturn(metadata)

        val version = versionFileDtoConverter.convert(versionFileDto, null, null)

        val expectedVersion = Version(SWAGGER_SPECIFICATION, metadata)

        assertThat(version.content).isEqualTo(expectedVersion.content)
        assertThat(version.metadata).isEqualTo(expectedVersion.metadata)
    }

    @Test
    fun convert_shouldReturnRemoteSpecification() {
        val versionFileDto = VersionFileDto(null,
            SWAGGER_REMOTE,
            id = UUID_STRING
        )

        given(specificationFileService.retrieveFile(SWAGGER_REMOTE)).willReturn(SWAGGER_SPECIFICATION)
        given(specificationFileService.getLocalOrRemoteFileContent(versionFileDto)).willReturn(SWAGGER_SPECIFICATION)

        given(specificationDataService.makeSpecificationMetaData(SWAGGER_SPECIFICATION, ServiceId(UUID_STRING), versionFileDto.fileUrl)).willReturn(metadata)

        val version = versionFileDtoConverter.convert(versionFileDto, null, null)

        val expectedVersion = Version(SWAGGER_SPECIFICATION, metadata)

        assertThat(version.content).isEqualTo(expectedVersion.content)
        assertThat(version.metadata).isEqualTo(expectedVersion.metadata)
    }
}