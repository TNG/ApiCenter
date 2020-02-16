package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.entity.ApplicationEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class ApplicationMapperUnitTest {

    private val applicationMapper = ApplicationMapper()

    @Test
    fun `should map applicationDto to applicationEntity`() {
        val applicationDto = ApplicationDto(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationName", "applicationDescription", "applicationContact")

        val applicationEntity = applicationMapper.toEntity(applicationDto)

        assertThat(applicationEntity).isEqualTo(ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationName", "applicationDescription", "applicationContact"))
    }

    @Test
    fun `should map applicationEntity to applicationDto`() {
        val applicationEntity = ApplicationEntity(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationName", "applicationDescription", "applicationContact")

        val applicationDto = applicationMapper.toDto(applicationEntity)

        assertThat(applicationDto).isEqualTo(ApplicationDto(UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673"), "applicationName", "applicationDescription", "applicationContact"))
    }
}
