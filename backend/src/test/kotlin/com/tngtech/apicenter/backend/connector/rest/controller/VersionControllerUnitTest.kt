package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionFileDtoMapper
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class VersionControllerUnitTest {

    private val versionPersistenceService: VersionPersistenceService = mock()

    private val versionFileDtoMapper: VersionFileDtoMapper = mock()

    private val versionController = VersionController(versionPersistenceService, versionFileDtoMapper)

    private val specificationId = "7de07d27-eedb-4290-881a-6a402a81dd0f"

    private val metadata = VersionMetaData(ServiceId(specificationId), "Swagger Petstore", "1.0.0", "Description", ApiLanguage.OPENAPI, null)

    private val version = Version("Content", metadata)

    private val versionDto = VersionDto("Content", metadata)

    @Test
    fun findOne_shouldReturnVersionDto() {
        given(versionPersistenceService.findOne(ServiceId(specificationId), "1.0")).willReturn(version)
        given(versionFileDtoMapper.fromDomain(version)).willReturn(versionDto)

        assertThat(versionController.findVersion(specificationId, "1.0")).isEqualTo(versionDto)
    }

    @Test
    fun delete_shouldDeleteVersion() {
        versionController.deleteVersion(specificationId, "1.0")

        verify(versionPersistenceService).delete(ServiceId(specificationId), "1.0")
    }

}