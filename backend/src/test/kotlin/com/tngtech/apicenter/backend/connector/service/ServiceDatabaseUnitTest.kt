package com.tngtech.apicenter.backend.connector.service

import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import com.tngtech.apicenter.backend.connector.database.mapper.ServiceEntityMapper
import com.tngtech.apicenter.backend.connector.database.repository.ServiceRepository
import com.tngtech.apicenter.backend.connector.database.service.ServiceDatabase
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import java.util.*
import javax.persistence.EntityManager

@RunWith(MockitoJUnitRunner::class)
internal class ServiceDatabaseUnitTest {

    private val serviceRepository: ServiceRepository = mock()

    private val entityManager: EntityManager = mock()

    private val serviceEntityMapper: ServiceEntityMapper = mock()

    private val serviceDatabase: ServiceDatabase =
        ServiceDatabase(serviceRepository, entityManager, serviceEntityMapper)

    private val id = "e33dc111-3dd6-40f4-9c54-a64f6b10ab49"

    @Test
    fun delete_shouldDeleteObject() {
        val uuid = ServiceId(UUID.randomUUID().toString())

        serviceDatabase.delete(uuid)
        verify(serviceRepository).deleteById(uuid.id)
    }

    @Test
    fun exists_shouldCheckForExistence() {
        val uuid = ServiceId(id)

        given(serviceRepository.existsById(uuid.id)).willReturn(true)

        assertThat(serviceDatabase.exists(uuid)).isTrue()

        verify(serviceRepository).existsById(uuid.id)
    }
}