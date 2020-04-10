package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.mapper.toDto
import com.tngtech.apicenter.mapper.toEntity
import com.tngtech.apicenter.repository.ApplicationRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
class ApplicationService(private val applicationRepository: ApplicationRepository) {

    fun getApplications(): List<ApplicationDto> = applicationRepository.findAll().map { it.toDto() }

    fun createApplication(applicationDto: ApplicationDto): ApplicationDto {
        val applicationEntity = applicationDto.toEntity()

        val createdApplication = applicationRepository.save(applicationEntity)
        return createdApplication.toDto()
    }

    fun updateApplication(applicationId: UUID, applicationDto: ApplicationDto): ApplicationDto {
        val applicationDtoToStore = ApplicationDto(applicationId, applicationDto.name, applicationDto.description, applicationDto.contact)

        val updatedApplicationEntity = applicationRepository.save(applicationDtoToStore.toEntity())

        return updatedApplicationEntity.toDto()
    }

    fun deleteApplication(applicationId: UUID) {
        try {
            return applicationRepository.deleteById(applicationId)
        } catch (exception: EmptyResultDataAccessException) {
            throw EntityNotFoundException().initCause(exception)
        }
    }

    fun getApplication(applicationId: UUID): ApplicationDto {
        val application = applicationRepository.findByIdOrNull(applicationId) ?: throw EntityNotFoundException()

        return application.toDto()
    }
}
