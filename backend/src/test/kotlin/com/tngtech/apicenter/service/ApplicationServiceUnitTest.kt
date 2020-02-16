package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity
import com.tngtech.apicenter.mapper.ApplicationMapper
import com.tngtech.apicenter.repository.ApplicationRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ApplicationServiceUnitTest {

    @InjectMocks
    private lateinit var applicationService: ApplicationService

    @Mock
    private lateinit var applicationMapper: ApplicationMapper

    @Mock
    private lateinit var applicationRepository: ApplicationRepository

    @Test
    fun `when creating an application it should create application and return created application`() {
        val newApplicationDto = ApplicationDto(null, "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val mappedApplicationEntity = ApplicationEntity(null, "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val createdApplicationEntity = ApplicationEntity("newApplicationId", "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val createdApplicationDto = ApplicationDto("newApplicationId", "newApplicationName", "newApplicationDescription", "newApplicationContact")

        given(applicationMapper.toEntity(newApplicationDto)).willReturn(mappedApplicationEntity)
        given(applicationMapper.toDto(createdApplicationEntity)).willReturn(createdApplicationDto)
        given(applicationRepository.save(mappedApplicationEntity)).willReturn(createdApplicationEntity)

        val createdApplication = applicationService.createApplication(newApplicationDto)

        verify(applicationMapper).toEntity(newApplicationDto)
        verify(applicationMapper).toDto(createdApplicationEntity)
        verify(applicationRepository).save(mappedApplicationEntity)

        assertThat(createdApplication.id).isEqualTo("newApplicationId")
        assertThat(createdApplication.name).isEqualTo("newApplicationName")
        assertThat(createdApplication.description).isEqualTo("newApplicationDescription")
        assertThat(createdApplication.contact).isEqualTo("newApplicationContact")
    }

    @Test
    fun `when requested all applications it should return all applications`() {
        val applicationEntityA = ApplicationEntity("id1", "applicationA", "descriptionA", "contactA")
        val applicationEntityB = ApplicationEntity("id2", "applicationB", "descriptionB", "contactB")
        val applicationDtoA = ApplicationDto("id1", "applicationA", "descriptionA", "contactA")
        val applicationDtoB = ApplicationDto("id2", "applicationB", "descriptionB", "contactB")

        given(applicationRepository.findAll()).willReturn(listOf(applicationEntityA, applicationEntityB))
        given(applicationMapper.toDto(applicationEntityA)).willReturn(applicationDtoA)
        given(applicationMapper.toDto(applicationEntityB)).willReturn(applicationDtoB)

        val applications = applicationService.getApplications()

        verify(applicationRepository).findAll()
        verify(applicationMapper).toDto(applicationEntityA)
        verify(applicationMapper).toDto(applicationEntityB)

        assertThat(applications).containsOnly(applicationDtoA, applicationDtoB)
    }
}
