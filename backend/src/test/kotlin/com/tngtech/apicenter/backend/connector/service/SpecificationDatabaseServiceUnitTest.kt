package com.tngtech.apicenter.backend.connector.service

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionEntity
import com.tngtech.apicenter.backend.connector.database.entity.VersionId
import com.tngtech.apicenter.backend.connector.database.mapper.SpecificationEntityMapper
import com.tngtech.apicenter.backend.connector.database.mapper.VersionEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.connector.database.service.SpecificationDatabaseService
import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.persistence.EntityManager

@RunWith(MockitoJUnitRunner::class)
internal class SpecificationDatabaseServiceUnitTest {

    private val specificationRepository: SpecificationRepository = mock()

    private val entityManager: EntityManager = mock()

    private val specificationEntityMapper: SpecificationEntityMapper = mock()

    private val versionEntityMapper: VersionEntityMapper = mock()

    private val specificationDatabaseService: SpecificationDatabaseService =
        SpecificationDatabaseService(specificationRepository, entityManager, specificationEntityMapper, versionEntityMapper)

    private val id = "e33dc111-3dd6-40f4-9c54-a64f6b10ab49"

    @Test
    fun save_shouldSaveObjects() {
        val specification = Specification(
            ServiceId(id),
            "Spec",
            "Description",
            listOf(Version("{\"json\": \"true\"}", VersionMetaData(
                    ServiceId("e33dc111-3dd6-40f4-9c54-a64f6b10ab49"),
                    "Spec", "1.0.0", "Description", ApiLanguage.OPENAPI, null))),
            "http://swaggerpetstore.com/docs"
        )

        val specificationEntity = SpecificationEntity(
                id,
            "Spec",
            "Description",
            listOf(VersionEntity(VersionId(id, "1.0.0"), "{\"json\": \"true\"}", "Spec", "Description", ApiLanguage.OPENAPI, "", null, null)),
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
        val uuid = ServiceId(UUID.randomUUID().toString())

        specificationDatabaseService.delete(uuid)
        verify(specificationRepository).deleteById(uuid.id)
    }

    @Test
    fun exists_shouldCheckForExistence() {
        val uuid = ServiceId(id)

        given(specificationRepository.existsById(uuid.id)).willReturn(true)

        assertThat(specificationDatabaseService.exists(uuid)).isTrue()

        verify(specificationRepository).existsById(uuid.id)
    }
}