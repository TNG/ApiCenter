package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity
import org.springframework.stereotype.Component

@Component
class ApplicationMapper {

    fun toDto(applicationEntity: ApplicationEntity) = ApplicationDto("", "", "", "")

    fun toEntity(applicationDto: ApplicationDto) = ApplicationEntity("", "", "", "")

}
