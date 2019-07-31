package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.qdesrame.openapi.diff.OpenApiCompare
import com.qdesrame.openapi.diff.model.ChangedOpenApi
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.handler.SpecificationHandler
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

private const val MEDIA_TYPE_YAML = "application/yml"

@RestController
class SpecificationController constructor(private val specificationHandler: SpecificationHandler,
                                          private val specificationFileDtoMapper: SpecificationFileDtoMapper) {

    @GetMapping(
            "/api/v1/service/{serviceId}/version/{version}",
            produces = [MediaType.APPLICATION_JSON_VALUE, MEDIA_TYPE_YAML],
            headers = ["Accept=" + MediaType.APPLICATION_JSON_VALUE, "Accept=" + MEDIA_TYPE_YAML]
    )
    fun findSpecification(
            @PathVariable serviceId: String,
            @PathVariable version: String,
            @RequestHeader(value = "Accept", defaultValue = MediaType.APPLICATION_JSON_VALUE) accept: String = MediaType.APPLICATION_JSON_VALUE
    ): SpecificationDto {
        val specification = specificationHandler.findOne(ServiceId(serviceId), version)
                ?: throw SpecificationNotFoundException(serviceId, version)
        return specificationFileDtoMapper.fromDomain(convertByMediaType(accept, specification))
    }

    private fun convertByMediaType(accept: String, specification: Specification): Specification {
        return if (accept == MEDIA_TYPE_YAML) {
            val jsonNodeTree = ObjectMapper().readTree(specification.content)
            val jsonAsYaml = YAMLMapper().writeValueAsString(jsonNodeTree)
            Specification(jsonAsYaml, specification.metadata)
        } else {
            specification
        }
    }

    @DeleteMapping("/api/v1/service/{serviceId}/version/{version}")
    fun deleteSpecification(@PathVariable serviceId: String, @PathVariable version: String) {
        specificationHandler.delete(ServiceId(serviceId), version)
    }

    @GetMapping("/api/v1/service/{serviceId}/diff/{version1}/with/{version2}")
    fun generateDiff(@PathVariable serviceId: String, @PathVariable version1: String, @PathVariable version2: String): ChangedOpenApi {
        val specification1 = specificationHandler.findOne(ServiceId(serviceId), version1)
                ?: throw SpecificationNotFoundException(serviceId, version1)
        val specification2 = specificationHandler.findOne(ServiceId(serviceId), version2)
                ?: throw SpecificationNotFoundException(serviceId, version2)
        return OpenApiCompare.fromContents(specification1.content, specification2.content)
    }
}
