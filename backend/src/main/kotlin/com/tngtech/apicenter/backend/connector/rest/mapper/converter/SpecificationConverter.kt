package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SpecificationConverter constructor(
    private val specificationFileService: SpecificationFileService,
    private val specificationDataService: SpecificationDataService
) :
    CustomConverter<SpecificationFileDto, Specification>() {

    override fun convert(
        specificationFileDto: SpecificationFileDto,
        destinationType: Type<out Specification>?,
        mappingContext: MappingContext?
    ): Specification {
        var fileContent = specificationFileDto.fileContent ?: ""
        val dtoMetadata = specificationFileDto.metaData
        val isGraphQLFile = dtoMetadata != null

        if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl != "") {
            fileContent = specificationFileService.retrieveFile(specificationFileDto.fileUrl)
        }

        val content = if (isGraphQLFile) fileContent else specificationDataService.parseFileContent(fileContent)
        val uuid = specificationFileDto.id ?: UUID.randomUUID()

        // If metadata is present, we use it. Otherwise, we build one from reading the content the client sends
        val metadata = dtoMetadata ?: SpecificationMetaData(
            specificationDataService.readTitle(content),
            specificationDataService.readVersion(content),
            specificationDataService.readDescription(content),
            ApiLanguage.OPENAPI,
            null
        )

        return Specification(
            uuid,
            metadata.title,
            metadata.description,
            listOf(Version(content, metadata)),
            specificationFileDto.fileUrl
        )
    }
}