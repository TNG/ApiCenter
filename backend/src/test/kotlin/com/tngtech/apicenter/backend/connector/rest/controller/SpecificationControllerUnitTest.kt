package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetadata
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistence
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class SpecificationControllerUnitTest {

    private val specificationPersistence: SpecificationPersistence = mock()

    private val specificationFileDtoMapper: SpecificationFileDtoMapper = mock()

    private val versionController = SpecificationController(specificationPersistence, specificationFileDtoMapper)

    private val serviceId = "7de07d27-eedb-4290-881a-6a402a81dd0f"

    private val metadata = SpecificationMetadata(ServiceId(serviceId), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    private val version = Specification("Content", metadata)

    private val versionDto = SpecificationDto("Content", metadata)

    @Test
    fun findOne_shouldReturnVersionDto() {
        given(specificationPersistence.findOne(ServiceId(serviceId), "1.0")).willReturn(version)
        given(specificationFileDtoMapper.fromDomain(version)).willReturn(versionDto)

        assertThat(versionController.findSpecification(serviceId, "1.0")).isEqualTo(versionDto)
    }

    @Test
    fun delete_shouldDeleteVersion() {
        versionController.deleteSpecification(serviceId, "1.0")

        verify(specificationPersistence).delete(ServiceId(serviceId), "1.0")
    }

}