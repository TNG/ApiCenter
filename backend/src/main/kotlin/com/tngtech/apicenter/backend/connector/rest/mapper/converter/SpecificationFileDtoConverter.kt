package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataParser
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileDownloader
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedServiceIdException
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component
import java.util.*

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

        val idFromUpload = specificationDataParser.extractId(parsedFileContent)
        val idFromPath = specificationFileDto.id
        val serviceId = getConsistentServiceId(idFromUpload, idFromPath)

        val metadata = specificationFileDto.metadata ?:
            specificationDataParser.makeSpecificationMetadata(parsedFileContent, serviceId, specificationFileDto.fileUrl)

        return Specification(parsedFileContent, metadata)
    }

    private fun getConsistentServiceId(idFromUpload: String?, idFromPath: String?): ServiceId {
        if (idFromUpload != null && idFromPath != null && idFromUpload != idFromPath) {
            throw MismatchedServiceIdException(idFromUpload, idFromPath)
        }
        return ServiceId(idFromUpload ?: idFromPath ?: UUID.randomUUID().toString())
    }
}