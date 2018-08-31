package com.tngtech.apicenter.backend.connector.database

import com.tngtech.apicenter.backend.connector.database.entity.SpecificationEntity
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID

internal class SpecificationEntityUnitTest {

    companion object {
        const val SPEC_UUID = "e33dc111-3dd6-40f4-9c54-a64f6b10ab49"
        const val JSON_CONTENT = "{\"json\": \"true\"}"
    }

    @Test
    fun fromDomain_shouldReturnDtoObject() {
        val specification = Specification(
            UUID.fromString(SPEC_UUID),
            "Spec",
            "Description",
            Version("1.0.0"),
            JSON_CONTENT,
            null
        )

        val specificationEntity = SpecificationEntity.fromDomain(specification)

        assertThat(specificationEntity).isEqualTo(
            SpecificationEntity(
                UUID.fromString(SPEC_UUID),
                "Spec",
                "Description",
                "1.0.0",
                JSON_CONTENT,
                null
            )
        )
    }
}