package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataParser
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileDownloader
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class SpecificationFileDtoConverter constructor(
        private val specificationFileDownloader: SpecificationFileDownloader,
        private val specificationDataParser: SpecificationDataParser
) :
    CustomConverter<SpecificationFileDto, Specification>() {

    override fun convert(
            specificationFileDto: SpecificationFileDto,
            destinationType: Type<out Specification>?,
            mappingContext: MappingContext?
    ): Specification {
        val fileContent = specificationFileDownloader.getLocalOrRemoteFileContent(specificationFileDto)

        val parsedFileContent = if (specificationFileDto.metadata != null) fileContent
                                else specificationDataParser.parseFileContent(fileContent)

        val metadata = specificationDataParser.makeSpecificationMetadata(
                parsedFileContent,
                specificationFileDto.id,
                specificationFileDto.metadata)

        return Specification(parsedFileContent, metadata)
    }

}