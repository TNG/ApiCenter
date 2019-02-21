package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
import com.tngtech.apicenter.backend.domain.entity.APILanguage
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
        val metaData = specificationFileDto.metaData
        val isGraphQLFile = metaData != null

        if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl != "") {
            fileContent = specificationFileService.retrieveFile(specificationFileDto.fileUrl)
        }

        val content = if (isGraphQLFile) fileContent else specificationDataService.parseFileContent(fileContent)
        val uuid = specificationFileDto.id ?: UUID.randomUUID()

        val title = metaData?.title?: specificationDataService.readTitle(content)
        val description = metaData?.description?: specificationDataService.readDescription(content)
        val language = if (isGraphQLFile) APILanguage.GRAPHQL else APILanguage.OPENAPI
        val version = metaData?.version?: specificationDataService.readVersion(content)

        return Specification(
            uuid,
            title,
            description,
            listOf(Version(version, content, language)),
            specificationFileDto.fileUrl
        )
    }
}