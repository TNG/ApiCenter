package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.handler.SpecificationHandler
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID

internal class SpecificationControllerUnitTest {

    companion object {
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\",\"description\":\"Description\"}}"
        const val UUID_STRING = "65d8491f-e602-40fc-a595-45e75f690df1"
    }

    private val specificationHandler: SpecificationHandler = mock()

    private val synchronizationService: SynchronizationService = mock()

    private val specificationDtoMapper: SpecificationDtoMapper = mock()

    private val specificationController: SpecificationController =
        SpecificationController(
            specificationHandler,
            synchronizationService,
            specificationDtoMapper
        )

    @Test
    fun uploadSpecification_shouldReturnDto() {
        val specificationFileDto =
            SpecificationFileDto(SWAGGER_SPECIFICATION)

        val specification = Specification(
            UUID.fromString(UUID_STRING),
            "Swagger Petstore",
            "Description",
            listOf(Version("1.0.0", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            null
        )
        val specificationDto = SpecificationDto(
            UUID.fromString(UUID_STRING),
            "Swagger Petstore",
            "Description",
            listOf(VersionDto("1.0.0", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            null
        )

        given(specificationDtoMapper.toDomain(specificationFileDto)).willReturn(specification)
        given(specificationDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        val returnedSpecificationDto = specificationController.uploadSpecification(specificationFileDto)

        assertThat(returnedSpecificationDto).isEqualTo(specificationDto)
    }

    @Test
    fun updateSpecification_shouldReturnDto() {
        val specificationFileDto =
            SpecificationFileDto(
                SWAGGER_SPECIFICATION,
                null,
                null,
                UUID.fromString(UUID_STRING)
            )
        val specification = Specification(
            UUID.fromString(UUID_STRING),
            "Swagger Petstore",
            "Description",
            listOf(Version("1.0.0", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            null
        )
        val specificationDto = SpecificationDto(
            UUID.fromString(UUID_STRING),
            "Swagger Petstore",
            "Description",
            listOf(VersionDto("1.0.0", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            null
        )

        given(specificationDtoMapper.toDomain(specificationFileDto)).willReturn(specification)
        given(specificationDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        val returnedSpecificationDto = specificationController.updateSpecification(specificationFileDto,
            UUID_STRING
        )

        assertThat(returnedSpecificationDto).isEqualTo(
            SpecificationDto(
                UUID.fromString(UUID_STRING),
                "Swagger Petstore",
                "Description",
                listOf(VersionDto("1.0.0", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
                null
            )
        )
    }

    @Test
    fun findAllSpecifications_shouldReturnSpecifications() {
        val uuid = UUID.randomUUID()

        val specification = Specification(
            uuid,
            "Test",
            "Description",
            listOf(Version("v2", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            "http://swaggerpetstore.com/docs"
        )
        val specificationDto = SpecificationDto(
            uuid,
            "Test",
            "Description",
            listOf(VersionDto("v2", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
            "http://swaggerpetstore.com/docs"
        )

        given(specificationDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        given(specificationHandler.findAll()).willReturn(
            arrayListOf(
                Specification(
                    uuid,
                    "Test",
                    "Description",
                    listOf(Version("v2", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
                    "http://swaggerpetstore.com/docs"
                )
            )
        )

        assertThat(specificationController.findAllSpecifications()).containsOnly(
            SpecificationDto(
                uuid, "Test",
                "Description",
                listOf(VersionDto("v2", SWAGGER_SPECIFICATION, ApiLanguage.OPENAPI)),
                "http://swaggerpetstore.com/docs"
            )
        )
    }
}