package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
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

        if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl != "") {
            fileContent = specificationFileService.retrieveFile(specificationFileDto.fileUrl)
        }

        val content = specificationDataService.parseFileContent(fileContent)
        val uuid = specificationFileDto.id ?: UUID.randomUUID()

        return Specification(
            uuid,
            specificationDataService.readTitle(content),
            specificationDataService.readDescription(content),
            Version(specificationDataService.readVersion(content)),
            content,
            specificationFileDto.fileUrl
        )
    }
}