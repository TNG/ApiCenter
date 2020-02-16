package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.mapper.ApplicationMapper
import com.tngtech.apicenter.repository.ApplicationRepository
import org.springframework.stereotype.Service

@Service
class ApplicationService(private val applicationMapper: ApplicationMapper, private val applicationRepository: ApplicationRepository) {

    fun getApplications(): List<ApplicationDto> = applicationRepository.findAll().map { application -> applicationMapper.toDto(application) }

    fun createApplication(applicationDto: ApplicationDto): ApplicationDto {
        val applicationEntity = applicationMapper.toEntity(applicationDto)

        val createdApplication = applicationRepository.save(applicationEntity)
        return applicationMapper.toDto(createdApplication)
    }

    fun updateApplication(applicationId: String, applicationDto: ApplicationDto) = ApplicationDto("", "", "", "")

    fun deleteApplication(applicationId: String) = ApplicationDto("", "", "", "")
}
