package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.domain.entity.*
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class SpecificationControllerUnitTest {

    private val specificationPersistor: SpecificationPersistor = mock()

    private val specificationFileDtoMapper: SpecificationFileDtoMapper = mock()

    private val specificationController = SpecificationController(specificationPersistor, specificationFileDtoMapper)

    private val serviceId = "7de07d27-eedb-4290-881a-6a402a81dd0f"

    private val metadata = SpecificationMetadata(ServiceId(serviceId), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, ReleaseType.RELEASE, null)

    private val specification = Specification("Content", metadata)

    private val specificationDto = SpecificationDto("Content", metadata)

    @Test
    fun findOne_shouldReturnSpecificationDto() {
        given(specificationPersistor.findOne(ServiceId(serviceId), "1.0")).willReturn(specification)
        given(specificationFileDtoMapper.fromDomain(specification)).willReturn(specificationDto)

        assertThat(specificationController.findSpecification(serviceId, "1.0")).isEqualTo(specificationDto)
    }

    @Test
    fun delete_shouldDeleteSpecification() {
        specificationController.deleteSpecification(serviceId, "1.0")

        verify(specificationPersistor).delete(ServiceId(serviceId), "1.0")
    }

}