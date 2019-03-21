package com.tngtech.apicenter.backend.connector.rest.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationParseException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class SpecificationDataService @Autowired constructor(
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

    fun readTitle(json: String): String {
        try {
            return JsonPath.read<String>(json, "$.info.title")
        } catch (exception: PathNotFoundException) {
            throw SpecificationParseException("Specification needs a title in the info section")
        } catch (exception: IllegalArgumentException) {
            throw SpecificationParseException("Specification could not be parsed as JSON")
        }
    }

    fun readVersion(json: String): String {
        try {
            return JsonPath.read<String>(json, "$.info.version")
        } catch (exception: PathNotFoundException) {
            throw SpecificationParseException("Specification needs a version")
        } catch (exception: IllegalArgumentException) {
            throw SpecificationParseException("Specification could not be parsed as JSON")
        }
    }

    fun readDescription(json: String): String? =
        // Not a required field in the OpenAPI spec
        try {
            JsonPath.read<String>(json, "$.info.description")
        } catch (exception: PathNotFoundException) {
            null
        }

    fun readId(json: String): String? =
        try {
            JsonPath.read<String>(json, "$.info.x-api-id")
        } catch (exception: PathNotFoundException) {
            null
        }
}