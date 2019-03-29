package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionFileDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

internal class SpecificationControllerUnitTest {

    companion object {
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\",\"description\":\"Description\"}}"
        const val UUID_STRING = "65d8491f-e602-40fc-a595-45e75f690df1"
    }
    private val metadata = VersionMetaData(ServiceId(UUID_STRING), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    private val specificationPersistenceService: SpecificationPersistenceService = mock()

    private val synchronizationService: SynchronizationService = mock()

    private val versionFileDtoMapper: VersionFileDtoMapper = mock()

    private val specificationDtoMapper: SpecificationDtoMapper = mock()

    private val specificationController: SpecificationController =
        SpecificationController(
            specificationPersistenceService,
            synchronizationService,
            versionFileDtoMapper,
            specificationDtoMapper
        )

    @Test
    fun updateSpecification_shouldReturnDto() {
        val versionFileDto =
            VersionFileDto(
                SWAGGER_SPECIFICATION,
                null,
                null,
                UUID_STRING
            )

        val version = Version(SWAGGER_SPECIFICATION, metadata)
        val versionDto = VersionDto(SWAGGER_SPECIFICATION, metadata)

        given(versionFileDtoMapper.toDomain(versionFileDto)).willReturn(version)
        given(versionFileDtoMapper.fromDomain(version)).willReturn(versionDto)

        val returnedVersionDto = specificationController.updateSpecification(versionFileDto,
            UUID_STRING
        )

        assertThat(returnedVersionDto).isEqualTo(versionDto)
    }

    @Test
    fun findAllSpecifications_shouldReturnSpecifications() {
        val uuid = UUID.randomUUID().toString()

        val specification = Specification(
            ServiceId(uuid),
            "Test",
            "Description",
            listOf(Version(SWAGGER_SPECIFICATION, metadata)),
            "http://swaggerpetstore.com/docs"
        )
        val specificationDto = SpecificationDto(
            uuid,
            "Test",
            "Description",
            listOf(VersionDto(SWAGGER_SPECIFICATION, metadata)),
            "http://swaggerpetstore.com/docs"
        )

        given(specificationDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        given(specificationPersistenceService.findAll()).willReturn(
            arrayListOf(
                Specification(
                    ServiceId(uuid),
                    "Test",
                    "Description",
                    listOf(Version(SWAGGER_SPECIFICATION, metadata)),
                    "http://swaggerpetstore.com/docs"
                )
            )
        )

        assertThat(specificationController.findAllSpecifications()).containsOnly(
            SpecificationDto(
                uuid, "Test",
                "Description",
                listOf(VersionDto(SWAGGER_SPECIFICATION, metadata)),
                "http://swaggerpetstore.com/docs"
            )
        )
    }
}