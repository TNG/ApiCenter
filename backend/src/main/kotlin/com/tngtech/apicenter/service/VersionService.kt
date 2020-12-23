package com.tngtech.apicenter.service

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.tngtech.apicenter.dto.VersionDto
import com.tngtech.apicenter.dto.VersionFileDto
import com.tngtech.apicenter.entity.VersionEntity
import com.tngtech.apicenter.mapper.toDto
import com.tngtech.apicenter.mapper.toEntity
import com.tngtech.apicenter.repository.VersionRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityNotFoundException
import javax.validation.ValidationException

@Service
class VersionService(private val versionRepository: VersionRepository, private val interfaceService: InterfaceService) {

    private val jsonObjectMapper = ObjectMapper()
    private val yamlObjectMapper = ObjectMapper(YAMLFactory())

    fun createVersion(versionDto: VersionFileDto): VersionDto {
        val versionEntity = when {
            isJson(versionDto.fileContent) -> {
                mapNewFileToVersionEntity(versionDto, jsonObjectMapper)
            }
            isYaml(versionDto.fileContent) -> {
                mapNewFileToVersionEntity(versionDto, yamlObjectMapper)
            }
            else -> {
                throw ValidationException("Please provide a valid JSON or YAML file.")
            }
        }

        return versionRepository.save(versionEntity).toDto()
    }

    fun deleteVersion(versionId: UUID): Any {
        try {
            return versionRepository.deleteById(versionId)
        } catch (exception: EmptyResultDataAccessException) {
            throw EntityNotFoundException().initCause(exception)
        }
    }

    fun getVersions() = versionRepository.findAll().map { it.toDto() }

    private fun isJson(fileContent: String): Boolean {
        return try {
            jsonObjectMapper.readTree(fileContent)
            true
        } catch (exception: JsonProcessingException) {
            false
        }
    }

    private fun isYaml(fileContent: String): Boolean {
        return try {
            yamlObjectMapper.readTree(fileContent)
            true
        } catch (exception: JsonProcessingException) {
            false
        }
    }

    private fun mapNewFileToVersionEntity(versionDto: VersionFileDto, objectMapper: ObjectMapper): VersionEntity {
        val jsonNode = objectMapper.readTree(versionDto.fileContent).get("info")
        val title = jsonNode.get("title")?.textValue()
        val version = jsonNode.get("version")?.textValue()
        val description = jsonNode.get("description")?.textValue()

        val myInterface = interfaceService.getInterface(versionDto.interfaceId).toEntity()

        if (title == null) {
            throw ValidationException("Title may not be null in OpenAPI file")
        }
        if (version == null) {
            throw ValidationException("Version may not be null in OpenAPI file")
        }

        return VersionEntity(UUID.randomUUID(), title, version, description, versionDto.fileContent, myInterface)
    }
}
