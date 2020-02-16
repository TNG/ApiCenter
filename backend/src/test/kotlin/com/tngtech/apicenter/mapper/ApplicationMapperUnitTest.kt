package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApplicationMapperUnitTest {

    private val applicationMapper = ApplicationMapper()

    @Test
    fun `should map applicationDto to applicationEntity`() {
        val applicationDto = ApplicationDto("applicationId", "applicationName", "applicationDescription", "applicationContact")

        val applicationEntity = applicationMapper.toEntity(applicationDto)

        assertThat(applicationEntity).isEqualTo(ApplicationEntity("applicationId", "applicationName", "applicationDescription", "applicationContact"))
    }

    @Test
    fun `should map applicationEntity to applicationDto`() {
        val applicationEntity = ApplicationEntity("applicationId", "applicationName", "applicationDescription", "applicationContact")

        val applicationDto = applicationMapper.toDto(applicationEntity)

        assertThat(applicationDto).isEqualTo(ApplicationDto("applicationId", "applicationName", "applicationDescription", "applicationContact"))
    }
}
