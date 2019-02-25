package com.tngtech.apicenter.backend.connector.rest.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.PathNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.io.IOException

@Service
class SpecificationDataService @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val yamlMapper: YAMLMapper
) {

    fun parseFileContent(fileContent: String): String = when {
        isJson(fileContent) -> fileContent
        isYaml(fileContent) -> yamlToJson(fileContent)
        else -> String()
    }

    fun yamlToJson(yaml: String): String {
        val obj = yamlMapper.readValue(yaml, Any::class.java)
        return objectMapper.writeValueAsString(obj)
    }

    fun isYaml(yaml: String): Boolean {
        try {
            yamlMapper.readTree(yaml)

            return true
        } catch (e: IOException) {
            return false
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
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Specification needs a title", exception)
        }
    }

    fun readVersion(json: String): String {
        try {
            return JsonPath.read<String>(json, "$.info.version")
        } catch (exception: PathNotFoundException) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Specification needs a version", exception)
        }
    }

    fun readDescription(json: String): String? =
        try {
            JsonPath.read<String>(json, "$.info.description")
        } catch (exception: PathNotFoundException) {
            null
        }
}