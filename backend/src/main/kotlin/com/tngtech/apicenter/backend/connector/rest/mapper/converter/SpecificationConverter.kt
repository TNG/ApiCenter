package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationDataService
import com.tngtech.apicenter.backend.connector.rest.service.SpecificationFileService
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationUploadUrlMismatch
import com.tngtech.apicenter.backend.domain.exceptions.UnacceptableUserDefinedApiId
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
        var fileContent = specificationFileDto.fileContent ?: ""
        val dtoMetadata = specificationFileDto.metaData
        val isGraphQLFile = dtoMetadata != null

        if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl != "") {
            fileContent = specificationFileService.retrieveFile(specificationFileDto.fileUrl)
        }

        val content = if (isGraphQLFile) fileContent else specificationDataService.parseFileContent(fileContent)

        val userDefinedId = specificationDataService.readId(content)
        val urlPathId = specificationFileDto.id

        if (userDefinedId?.let { userId -> !isAcceptable(userId) } ?: run { false } ) {
            throw UnacceptableUserDefinedApiId(userDefinedId!!)
        }

        if (userDefinedId?.let { userId -> urlPathId?.let { urlId -> urlId != userId } } ?: run { false } ) {
            throw SpecificationUploadUrlMismatch(userDefinedId!!, urlPathId!!)
        }

        val uuid = userDefinedId ?: urlPathId ?: UUID.randomUUID().toString()

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

    private fun isAcceptable(userId: String): Boolean {
        // This is quite conservative.
        // An 'unacceptable' ID for this purpose is one that cannot be subsequently retrieved from the repository
        //   because the ID in the browser URI path does not match the ID in the specification
        // Some characters are escaped automatically, eg. '/' (http://localhost:4200/specifications/unique%2Fidentifier/0.1.8)
        // Some special characters (äöü§) are not, but still result in the same problem
        //   perhaps these are escaped at a different pipeline stage of URI processing
        return userId.matches("^[\\w_\\-]+$".toRegex())
    }
}