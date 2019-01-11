package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper

import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.handler.VersionHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@RestController
class VersionController constructor(private val versionHandler: VersionHandler, private val versionDtoMapper: VersionDtoMapper) {
    @GetMapping("/specifications/{specificationId}/versions/{version}")
    fun findVersion(@PathVariable specificationId: UUID, @PathVariable version: String): VersionDto = versionDtoMapper.fromDomain(versionHandler.findOne(specificationId, version))


    @GetMapping("/static/{specificationId}/versions/{version}")
    fun downloadVersion(@PathVariable specificationId: UUID, @PathVariable version: String, response: HttpServletResponse) {
        logger.info("Download version called")
        // For now, this function returns a static response, just to test the routing
        // The following lines may come in handy later
        val foundVersion = versionHandler.findOne(specificationId, version)
        
        val jsonNodeTree: JsonNode = ObjectMapper().readTree(foundVersion.content);
        val jsonAsYaml: String = YAMLMapper().writeValueAsString(jsonNodeTree);
        // Needs error handling in case the conversion fails
        
        // Write the API content to the reponse
        response.setContentType("text/yaml")
        response.getWriter().write(jsonAsYaml)
        response.getWriter().flush()

        response.flushBuffer()
    }
    

    @DeleteMapping("/specifications/{specificationId}/versions/{version}")
    fun deleteVersion(@PathVariable specificationId: UUID, @PathVariable version: String) {
        logger.info("Delete version called")
        versionHandler.delete(specificationId, version)
    }
}