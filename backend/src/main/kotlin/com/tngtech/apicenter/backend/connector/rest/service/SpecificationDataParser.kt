package com.tngtech.apicenter.backend.connector.rest.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.github.zafarkhaja.semver.Version as SemVer
import com.github.zafarkhaja.semver.UnexpectedCharacterException
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileMetadata
import com.tngtech.apicenter.backend.domain.entity.*
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedServiceIdException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationParseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException
import java.util.*

@Service
class SpecificationDataParser @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val yamlMapper: YAMLMapper
) {

    fun parseFileContent(fileContent: String): String = when {
        isJson(fileContent) -> fileContent
        isYaml(fileContent) -> yamlToJson(fileContent)
        else -> fileContent
    }

    fun yamlToJson(yaml: String): String {
        val obj = yamlMapper.readValue(yaml, Any::class.java)
        return objectMapper.writeValueAsString(obj)
    }

    fun isYaml(yaml: String): Boolean {
        return try {
            yamlMapper.readTree(yaml)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun isJson(json: String): Boolean {
        return try {
            objectMapper.readTree(json)
            true
        } catch (e: IOException) {
            false
        }
    }

    fun extractTitle(json: String): String {
        try {
            return JsonPath.read<String>(json, "$.info.title")
        } catch (exception: PathNotFoundException) {
            throw SpecificationParseException("Service needs a title in the info section")
        } catch (exception: IllegalArgumentException) {
            throw SpecificationParseException("Service could not be parsed as JSON")
        }
    }

    fun extractVersion(json: String): String {
        try {
            return JsonPath.read<String>(json, "$.info.version")
        } catch (exception: PathNotFoundException) {
            throw SpecificationParseException("Service needs a version")
        } catch (exception: IllegalArgumentException) {
            throw SpecificationParseException("Service could not be parsed as JSON")
        }
    }

    fun extractDescription(json: String): String? =
        try {
            JsonPath.read<String>(json, "$.info.description")
        } catch (exception: PathNotFoundException) {
            null
        }
    fun extractId(json: String): String? =
        try {
            val parsedJson = objectMapper.readTree(json)
            val jsonNode = parsedJson.at("/info/x-api-id")
            if (jsonNode.isMissingNode) {
                null
            } else {
                jsonNode.asText()
            }
        } catch (exception: IOException) {
            null
        }

    fun makeSpecificationMetadata(fileContent: String,
                                  idFromPath: String? = null,
                                  metadata: SpecificationFileMetadata? = null
    ): SpecificationMetadata {
        val idFromUpload = extractId(fileContent)
        val serviceId = getConsistentServiceId(idFromUpload, idFromPath)
        val version = metadata?.version ?: extractVersion(fileContent)
        try {
            SemVer.valueOf(version)
        } catch (exception: UnexpectedCharacterException) {
            throw SpecificationParseException("Version must be SemVer formatted, see https://semver.org/")
        }

        return if (metadata != null) {
            SpecificationMetadata(
                    serviceId,
                    metadata.title,
                    version,
                    metadata.description,
                    ApiLanguage.GRAPHQL,
                    ReleaseType.fromVersionString(metadata.version),
                    metadata.endpointUrl
            )
        } else {
            SpecificationMetadata(
                    serviceId,
                    extractTitle(fileContent),
                    version,
                    extractDescription(fileContent),
                    ApiLanguage.OPENAPI,
                    ReleaseType.fromVersionString(version),
                    null
            )
        }

    }

    private fun getConsistentServiceId(idFromUpload: String?, idFromPath: String?): ServiceId {
        if (idFromUpload != null && idFromPath != null && idFromUpload != idFromPath) {
            throw MismatchedServiceIdException(idFromUpload, idFromPath)
        }
        return ServiceId(idFromUpload ?: idFromPath ?: UUID.randomUUID().toString())
    }
}
