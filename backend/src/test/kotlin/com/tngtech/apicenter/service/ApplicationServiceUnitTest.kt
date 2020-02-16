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
import java.util.*

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
        val createdApplicationEntity = ApplicationEntity(UUID.fromString("caa7f982-ff3d-40b5-95ff-b3743f53e795"), "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val createdApplicationDto = ApplicationDto(UUID.fromString("caa7f982-ff3d-40b5-95ff-b3743f53e795"), "newApplicationName", "newApplicationDescription", "newApplicationContact")

        given(applicationMapper.toEntity(newApplicationDto)).willReturn(mappedApplicationEntity)
        given(applicationMapper.toDto(createdApplicationEntity)).willReturn(createdApplicationDto)
        given(applicationRepository.save(mappedApplicationEntity)).willReturn(createdApplicationEntity)

        val createdApplication = applicationService.createApplication(newApplicationDto)

        verify(applicationMapper).toEntity(newApplicationDto)
        verify(applicationMapper).toDto(createdApplicationEntity)
        verify(applicationRepository).save(mappedApplicationEntity)

        assertThat(createdApplication.id).isEqualTo(UUID.fromString("caa7f982-ff3d-40b5-95ff-b3743f53e795"))
        assertThat(createdApplication.name).isEqualTo("newApplicationName")
        assertThat(createdApplication.description).isEqualTo("newApplicationDescription")
        assertThat(createdApplication.contact).isEqualTo("newApplicationContact")
    }

    @Test
    fun `when requested all applications it should return all applications`() {
        val applicationEntityA = ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationA", "descriptionA", "contactA")
        val applicationEntityB = ApplicationEntity(UUID.fromString("caa7f982-ff3d-40b5-95ff-b3743f53e795"), "applicationB", "descriptionB", "contactB")
        val applicationDtoA = ApplicationDto(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationA", "descriptionA", "contactA")
        val applicationDtoB = ApplicationDto(UUID.fromString("caa7f982-ff3d-40b5-95ff-b3743f53e795"), "applicationB", "descriptionB", "contactB")

        given(applicationRepository.findAll()).willReturn(listOf(applicationEntityA, applicationEntityB))
        given(applicationMapper.toDto(applicationEntityA)).willReturn(applicationDtoA)
        given(applicationMapper.toDto(applicationEntityB)).willReturn(applicationDtoB)

        val applications = applicationService.getApplications()

        verify(applicationRepository).findAll()
        verify(applicationMapper).toDto(applicationEntityA)
        verify(applicationMapper).toDto(applicationEntityB)

        assertThat(applications).containsOnly(applicationDtoA, applicationDtoB)
    }

    @Test
    fun `when updating an application it should update the application`() {
        val applicationDto = ApplicationDto(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val applicationEntity = ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "newApplicationName", "newApplicationDescription", "newApplicationContact")

        given(applicationRepository.save(applicationEntity)).willReturn(applicationEntity)
        given(applicationMapper.toDto(applicationEntity)).willReturn(applicationDto)
        given(applicationMapper.toEntity(applicationDto)).willReturn(applicationEntity)

        val updatedApplication = applicationService.updateApplication(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), applicationDto)

        verify(applicationRepository).save(applicationEntity)
        verify(applicationMapper).toDto(applicationEntity)
        verify(applicationMapper).toEntity(applicationDto)

        assertThat(updatedApplication).isEqualTo(applicationDto)
    }

    @Test
    fun `when updating an application without existing id it should set the id and update the application`() {
        val applicationDto = ApplicationDto(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "newApplicationName", "newApplicationDescription", "newApplicationContact")
        val applicationEntity = ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "newApplicationName", "newApplicationDescription", "newApplicationContact")

        given(applicationRepository.save(applicationEntity)).willReturn(applicationEntity)
        given(applicationMapper.toDto(applicationEntity)).willReturn(applicationDto)
        given(applicationMapper.toEntity(applicationDto)).willReturn(applicationEntity)

        val updatedApplication = applicationService.updateApplication(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), ApplicationDto(null, "newApplicationName", "newApplicationDescription", "newApplicationContact"))

        verify(applicationRepository).save(ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "newApplicationName", "newApplicationDescription", "newApplicationContact"))
        verify(applicationMapper).toDto(applicationEntity)
        verify(applicationMapper).toEntity(applicationDto)

        assertThat(updatedApplication).isEqualTo(applicationDto)
    }
}
