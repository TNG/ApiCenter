package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistence
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException

private const val MEDIA_TYPE_YAML = "application/yml"

@RestController
class SpecificationController constructor(private val specificationPersistence: SpecificationPersistence,
                                          private val specificationFileDtoMapper: SpecificationFileDtoMapper) {

    @RequestMapping("/api/v1/service/{serviceId}/version/{version}",
            produces = [MediaType.APPLICATION_JSON_VALUE,
                        MEDIA_TYPE_YAML],
            headers =  ["Accept=" + MediaType.APPLICATION_JSON_VALUE,
                        "Accept=" + MEDIA_TYPE_YAML],
            method =   [RequestMethod.GET])
    fun findSpecification(@PathVariable serviceId: String,
                          @PathVariable version: String,
                          @RequestHeader(value = "Accept",
                                   defaultValue = MediaType.APPLICATION_JSON_VALUE) accept: String = MediaType.APPLICATION_JSON_VALUE): SpecificationDto {
        // i.e. The integration test and unit test require the default specified in two different ways
        val specification = specificationPersistence.findOne(ServiceId(serviceId), version)
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
        specificationPersistence.delete(ServiceId(serviceId), version)
    }
}
