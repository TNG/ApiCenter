package com.tngtech.apicenter.backend.connector.rest.controller

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.handler.VersionHandler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID

internal class VersionControllerUnitTest {

    private val versionHandler: VersionHandler = mock()

    private val versionDtoMapper: VersionDtoMapper = mock()

    private val versionController = VersionController(versionHandler, versionDtoMapper)

    private val versionId = "7de07d27-eedb-4290-881a-6a402a81dd0f"

    private val version = Version(UUID.fromString(versionId), "1.0", "Content")

    private val versionDto = VersionDto(UUID.fromString(versionId), "1.0", "Content")

    @Test
    fun findOne_shouldReturnVersionDto() {
        given(versionHandler.findOne(UUID.fromString(versionId))).willReturn(version)
        given(versionDtoMapper.fromDomain(version)).willReturn(versionDto)

        assertThat(versionController.findVersion(UUID.fromString(versionId))).isEqualTo(versionDto)
    }

    @Test
    fun delete_shouldDeleteVersion() {
        versionController.deleteVersion(UUID.fromString(versionId))

        verify(versionHandler).delete(UUID.fromString(versionId))
    }

}