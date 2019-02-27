package com.tngtech.apicenter.backend.connector.service

import com.nhaarman.mockitokotlin2.given
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.connector.database.service.SpecificationDatabaseService
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.connector.database.mapper.SpecificationEntityMapper
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID
import javax.persistence.EntityManager

@RunWith(MockitoJUnitRunner::class)
internal class SpecificationDatabaseServiceUnitTest {

    private val specificationRepository: SpecificationRepository = mock()

    private val entityManager: EntityManager = mock()

    private val specificationEntityMapper: SpecificationEntityMapper = mock()

    private val specificationDatabaseService: SpecificationDatabaseService =
        SpecificationDatabaseService(specificationRepository, entityManager, specificationEntityMapper)

    private val versionId = "5aa40ba9-7e26-44de-81ec-f545d1f178aa"

    @Test
    fun save_shouldSaveObjects() {
        val specification = Specification(
            UUID.fromString("e33dc111-3dd6-40f4-9c54-a64f6b10ab49"),
            "Spec",
            "Description",
            listOf(Version("{\"json\": \"true\"}", SpecificationMetaData("Spec", "1.0.0", "Description", ApiLanguage.OPENAPI, null))),
            "http://swaggerpetstore.com/docs"
        )

        val specificationEntity = SpecificationEntity(
            UUID.fromString("e33dc111-3dd6-40f4-9c54-a64f6b10ab49"),
            "Spec",
            "Description",
            listOf(VersionEntity(VersionId(null, "1.0.0"), "{\"json\": \"true\"}", "Spec", "Description", ApiLanguage.OPENAPI, "", null, null)),
            "http://swaggerpetstore.com/docs"
        )

        given(specificationEntityMapper.fromDomain(specification)).willReturn(specificationEntity)

        specificationDatabaseService.save(specification)
        verify(specificationRepository).save(specificationEntity)
    }

    @Test
    fun findAll_shouldReturnObjects() {
        specificationDatabaseService.findAll()
        verify(specificationRepository).findAll()
    }

    @Test
    fun delete_shouldDeleteObject() {
        val uuid = UUID.randomUUID()

        specificationDatabaseService.delete(uuid)
        verify(specificationRepository).deleteById(uuid)
    }

    @Test
    fun exists_shouldCheckForExistence() {
        val uuid = UUID.fromString("e33dc111-3dd6-40f4-9c54-a64f6b10ab49")

        given(specificationRepository.existsById(uuid)).willReturn(true)

        assertThat(specificationDatabaseService.exists(uuid)).isTrue()

        verify(specificationRepository).existsById(uuid)
    }
}