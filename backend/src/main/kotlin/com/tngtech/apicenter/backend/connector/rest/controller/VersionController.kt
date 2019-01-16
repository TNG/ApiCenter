package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper

import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.handler.VersionHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@RestController
class VersionController constructor(private val versionHandler: VersionHandler, private val versionDtoMapper: VersionDtoMapper) {

    @RequestMapping("/specifications/{specificationId}/versions/{version}",
        produces = ["application/json", "application/yml"],
        headers = ["Accept=application/json", "Accept=application/yml"],
        method = [RequestMethod.GET])
    fun findVersion(@PathVariable specificationId: UUID, @PathVariable version: String, @RequestHeader("Accept") accept: String): VersionDto {
        val foundVersion = versionHandler.findOne(specificationId, version)
        if (accept == "application/yml") {
            logger.info("YAML requested")
            val jsonNodeTree: JsonNode = ObjectMapper().readTree(foundVersion.content)
            val jsonAsYaml: String = YAMLMapper().writeValueAsString(jsonNodeTree)
            logger.info(jsonAsYaml)
            return versionDtoMapper.fromDomain(Version(foundVersion.version, jsonAsYaml))
        } else {
            logger.info("JSON requested")
            return versionDtoMapper.fromDomain(foundVersion)
        }
    }

    @DeleteMapping("/specifications/{specificationId}/versions/{version}")
    fun deleteVersion(@PathVariable specificationId: UUID, @PathVariable version: String) {
        versionHandler.delete(specificationId, version)
    }
}