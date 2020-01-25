package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.ApplicationDto
import org.springframework.stereotype.Service

@Service
class ApplicationService {

    fun getApplications(): List<ApplicationDto> = emptyList()

    fun createApplication(applicationDto: ApplicationDto) = ApplicationDto("", "", "")

    fun updateApplication(applicationId: String, applicationDto: ApplicationDto) = ApplicationDto("", "", "")

    fun deleteApplication(applicationId: String) = ApplicationDto("", "", "")
}
