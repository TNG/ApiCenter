package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.config.ApiCenterProperties
import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.mapper.ServiceDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.RemoteServiceUpdater
import com.tngtech.apicenter.backend.domain.entity.*
import com.tngtech.apicenter.backend.domain.handler.ServiceHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

internal class ServiceControllerUnitTest {

    companion object {
        const val SWAGGER_SPECIFICATION =
            "{\"swagger\": \"2.0\", \"info\": {\"version\": \"1.0.0\",\"title\": \"Swagger Petstore\",\"description\":\"Description\"}}"
        const val UUID_STRING = "65d8491f-e602-40fc-a595-45e75f690df1"
    }
    private val metadata = SpecificationMetadata(ServiceId(UUID_STRING), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, ReleaseType.RELEASE, null)

    private val serviceHandler: ServiceHandler = mock()

    private val remoteServiceUpdater: RemoteServiceUpdater = mock()

    private val specificationFileDtoMapper: SpecificationFileDtoMapper = mock()

    private val serviceDtoMapper: ServiceDtoMapper = mock()

    private val serviceController: ServiceController =
        ServiceController(
            ApiCenterProperties(),
            serviceHandler,
            remoteServiceUpdater,
            specificationFileDtoMapper,
            serviceDtoMapper
        )

    @Test
    fun updateSpecification_shouldReturnDto() {
        val specificationFileDto =
            SpecificationFileDto(
                SWAGGER_SPECIFICATION,
                null,
                null,
                UUID_STRING
            )

        val specification = Specification(SWAGGER_SPECIFICATION, metadata)
        val specificationDto = SpecificationDto(SWAGGER_SPECIFICATION, metadata)

        given(specificationFileDtoMapper.toDomain(specificationFileDto)).willReturn(specification)
        given(specificationFileDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        val returnedSpecificationDto = serviceController.updateSpecification(specificationFileDto,
            UUID_STRING
        )

        assertThat(returnedSpecificationDto).isEqualTo(specificationDto)
    }

    @Test
    fun findAllServices_shouldReturnServices() {
        val uuid = UUID.randomUUID().toString()

        val service = Service(
            ServiceId(uuid),
            "Test",
            "Description",
            listOf(Specification(SWAGGER_SPECIFICATION, metadata)),
            "http://swaggerpetstore.com/docs"
        )
        val serviceDto = ServiceDto(
            uuid,
            "Test",
            "Description",
            listOf(SpecificationDto(SWAGGER_SPECIFICATION, metadata)),
            "http://swaggerpetstore.com/docs"
        )

        given(serviceHandler.findAll(0, 10)).willReturn(ResultPage(listOf(service), true))
        given(serviceDtoMapper.fromDomain(service)).willReturn(serviceDto)

        assertThat(serviceController.findAllServices("0").content).containsOnly(
            ServiceDto(
                uuid, "Test",
                "Description",
                listOf(SpecificationDto(SWAGGER_SPECIFICATION, metadata)),
                "http://swaggerpetstore.com/docs"
            )
        )
    }
}