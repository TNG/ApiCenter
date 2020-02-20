package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.mapper.ApplicationMapper
import com.tngtech.apicenter.repository.ApplicationRepository
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ApplicationService(private val applicationMapper: ApplicationMapper, private val applicationRepository: ApplicationRepository) {

    fun getApplications(): List<ApplicationDto> = applicationRepository.findAll().map { application -> applicationMapper.toDto(application) }

    fun createApplication(applicationDto: ApplicationDto): ApplicationDto {
        val applicationEntity = applicationMapper.toEntity(applicationDto)

        val createdApplication = applicationRepository.save(applicationEntity)
        return applicationMapper.toDto(createdApplication)
    }

    fun updateApplication(applicationId: UUID, applicationDto: ApplicationDto): ApplicationDto {
        val applicationDtoToStore = if (applicationDto.id === null) {
            ApplicationDto(applicationId, applicationDto.name, applicationDto.description, applicationDto.contact)
        } else {
            applicationDto
        }

        val updatedApplicationEntity = applicationRepository.save(applicationMapper.toEntity(applicationDtoToStore))

        return applicationMapper.toDto(updatedApplicationEntity)
    }

    fun deleteApplication(applicationId: String) = ApplicationDto(UUID.randomUUID(), "", "", "")
}
