package com.tngtech.apicenter.backend.connector.rest.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetadata
import com.tngtech.apicenter.backend.domain.entity.ApiLanguage
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationParseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

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

    fun extractId(json: String): String? {
        val parsedJson = objectMapper.readTree(json)
        val jsonNode = parsedJson.at("/info/x-api-id")
        return if (jsonNode.isMissingNode) {
            null
        } else {
            jsonNode.asText()
        }
    }

    fun makeSpecificationMetadata(fileContent: String, serviceId: ServiceId, endpointUrl: String?): SpecificationMetadata {
        return SpecificationMetadata(
                serviceId,
                extractTitle(fileContent),
                extractVersion(fileContent),
                extractDescription(fileContent),
                ApiLanguage.OPENAPI,
                endpointUrl
        )
    }
}