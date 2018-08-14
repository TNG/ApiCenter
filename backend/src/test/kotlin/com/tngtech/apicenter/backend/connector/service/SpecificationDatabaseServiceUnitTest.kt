package com.tngtech.apicenter.backend.connector.service

import com.nhaarman.mockitokotlin2.given
import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.connector.database.repository.SpecificationRepository
import com.tngtech.apicenter.backend.connector.database.service.SpecificationDatabaseService
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.UUID

@RunWith(MockitoJUnitRunner::class)
internal class SpecificationDatabaseServiceUnitTest {

    private val specificationRepository: SpecificationRepository = mock()

    private val specificationDatabaseService: SpecificationDatabaseService =
        SpecificationDatabaseService(specificationRepository)

    @Test
    fun save_shouldSaveObjects() {
        val specification = Specification(
            UUID.fromString("e33dc111-3dd6-40f4-9c54-a64f6b10ab49"),
            "Spec",
            Version("1.0.0"),
            "{\"json\": \"true\"}",
            "http://swaggerpetstore.com/docs"
        )

        val specificationEntity = SpecificationEntity(
            UUID.fromString("e33dc111-3dd6-40f4-9c54-a64f6b10ab49"),
            "Spec",
            "1.0.0",
            "{\"json\": \"true\"}",
            "http://swaggerpetstore.com/docs"
        )

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