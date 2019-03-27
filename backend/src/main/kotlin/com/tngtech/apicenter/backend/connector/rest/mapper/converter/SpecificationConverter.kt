package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedSpecificationIdException
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component
import java.util.*

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
        val fileContent = getLocalOrRemoteFileContent(specificationFileDto)

        val parsedFileContent = if (specificationFileDto.metaData != null) fileContent
                                else specificationDataService.parseFileContent(fileContent)

        val metadata = specificationFileDto.metaData ?: makeSpecificationMetaData(parsedFileContent)

        val idFromUpload = specificationDataService.extractId(parsedFileContent)
        val idFromPath = specificationFileDto.id
        val serviceId = getConsistentServiceId(idFromUpload, idFromPath)

        return Specification(
            serviceId,
            metadata.title,
            metadata.description,
            listOf(Version(parsedFileContent, metadata)),
            specificationFileDto.fileUrl
        )
    }

    private fun getLocalOrRemoteFileContent(specificationFileDto: SpecificationFileDto): String {
        return if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl != "") {
            specificationFileService.retrieveFile(specificationFileDto.fileUrl)
        } else {
            specificationFileDto.fileContent ?: ""
        }
    }

    private fun getConsistentServiceId(idFromUpload: String?, idFromPath: String?): ServiceId {
        if (idFromUpload != null && idFromPath != null && idFromUpload != idFromPath) {
            throw MismatchedSpecificationIdException(idFromUpload, idFromPath)
        }
        return ServiceId(idFromUpload ?: idFromPath ?: UUID.randomUUID().toString())
    }

    private fun makeSpecificationMetaData(fileContent: String): SpecificationMetaData {
        return SpecificationMetaData(
            specificationDataService.extractTitle(fileContent),
            specificationDataService.extractVersion(fileContent),
            specificationDataService.extractDescription(fileContent),
            ApiLanguage.OPENAPI,
            null
        )
    }
}