package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException

import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import java.util.UUID
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}
private const val MEDIA_TYPE_YAML = "application/yml"

@RestController
class VersionController constructor(private val versionPersistenceService: VersionPersistenceService, private val versionDtoMapper: VersionDtoMapper) {

    @RequestMapping("/api/v1/specifications/{specificationId}/versions/{version}",
            produces = [MediaType.APPLICATION_JSON_VALUE,
                        MEDIA_TYPE_YAML],
            headers =  ["Accept=" + MediaType.APPLICATION_JSON_VALUE,
                        "Accept=" + MEDIA_TYPE_YAML],
            method =   [RequestMethod.GET])
    fun findVersion(@PathVariable specificationId: UUID,
                    @PathVariable version: String,
                    @RequestHeader(value = "Accept",
                                   defaultValue = MediaType.APPLICATION_JSON_VALUE) accept: String = MediaType.APPLICATION_JSON_VALUE): VersionDto {
        // i.e. The integration test and unit test require the default specified in two different ways
        val foundVersion = versionPersistenceService.findOne(specificationId, version)
        if (foundVersion == null) {
            throw SpecificationNotFoundException(specificationId, version)
        } else if (accept == MEDIA_TYPE_YAML) {
            logger.info("Specification $specificationId version $version requested as YAML")
            val jsonNodeTree = ObjectMapper().readTree(foundVersion.content)
            val jsonAsYaml = YAMLMapper().writeValueAsString(jsonNodeTree)
            return versionDtoMapper.fromDomain(Version(jsonAsYaml, foundVersion.metadata))
        } else {
            logger.info("Specification $specificationId version $version requested as JSON")
            return versionDtoMapper.fromDomain(foundVersion)
        }
    }

    @DeleteMapping("/api/v1/specifications/{specificationId}/versions/{version}")
    fun deleteVersion(@PathVariable specificationId: UUID, @PathVariable version: String) {
        versionPersistenceService.delete(specificationId, version)
    }
}
