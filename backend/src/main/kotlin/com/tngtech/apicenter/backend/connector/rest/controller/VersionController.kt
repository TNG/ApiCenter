package com.tngtech.apicenter.backend.connector.rest.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.entity.Version
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.service.VersionPersistenceService
import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import org.springframework.web.bind.annotation.*
import org.springframework.http.MediaType
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.GiveUpOnAclException
import mu.KotlinLogging
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.Long.parseLong

private val logger = KotlinLogging.logger {}
private const val MEDIA_TYPE_YAML = "application/yml"

@RestController
class VersionController constructor(private val versionPersistenceService: VersionPersistenceService,
                                    private val versionDtoMapper: VersionDtoMapper,
                                    private val permissionManager: SpecificationPermissionManager,
                                    private val jwtAuthenticationProvider: JwtAuthenticationProvider) {

    @RequestMapping("/api/v1/specifications/{specificationId}/versions/{version}",
            produces = [MediaType.APPLICATION_JSON_VALUE,
                        MEDIA_TYPE_YAML],
            headers =  ["Accept=" + MediaType.APPLICATION_JSON_VALUE,
                        "Accept=" + MEDIA_TYPE_YAML],
            method =   [RequestMethod.GET])
    fun findVersion(@PathVariable specificationId: String,
                    @PathVariable version: String,
                    @RequestHeader(value = "Accept",
                                   defaultValue = MediaType.APPLICATION_JSON_VALUE) accept: String = MediaType.APPLICATION_JSON_VALUE): VersionDto {
        // i.e. The integration test and unit test require the default specified in two different ways
        val foundVersion = versionPersistenceService.findOne(ServiceId(specificationId), version)
                ?: throw SpecificationNotFoundException(specificationId, version)
        return versionDtoMapper.fromDomain(convertVersion(accept, foundVersion))
    }

    private fun convertVersion(accept: String, foundVersion: Version): Version {
        return if (accept == MEDIA_TYPE_YAML) {
            val jsonNodeTree = ObjectMapper().readTree(foundVersion.content)
            val jsonAsYaml = YAMLMapper().writeValueAsString(jsonNodeTree)
            Version(jsonAsYaml, foundVersion.metadata)
        } else {
            foundVersion
        }
    }

    @DeleteMapping("/api/v1/specifications/{specificationId}/versions/{version}")
    fun deleteVersion(@PathVariable specificationId: String, @PathVariable version: String) {
        versionPersistenceService.delete(ServiceId(specificationId), version)
    }

    @PostMapping("/api/v1/specifications/{specificationId}/versions/{version}/chmod/{principal}")
    fun chmodVersion(@PathVariable specificationId: String,
                     @PathVariable version: String,
                     @PathVariable principal: String,
                     @RequestHeader(value = "Authorization") authHeader: String,
                     @RequestParam(value = "read", required = false) grantRead: Boolean,
                     @RequestParam(value = "write", required = false) grantWrite: Boolean
    ) {
        // TODO: Return 400 when principal doesn't exist (should be one of user, group, or 'all', 'everyone' keyword)

        val username = jwtAuthenticationProvider.extractUsername(authHeader)
        val sid = PrincipalSid(username)
        logger.info(SecurityContextHolder.getContext().authentication.toString())
        val asLong = try { parseLong(specificationId) } catch (exc: NumberFormatException) { throw GiveUpOnAclException() }

        // exceptions may be thrown when attempting modifications, which are not yet handled
        if (grantRead) {
            permissionManager.addPermission(asLong, sid, BasePermission.READ)
        } else {
            permissionManager.removePermission(asLong, sid, BasePermission.READ)
        }

        if (grantWrite) {
            permissionManager.addPermission(asLong, sid, BasePermission.WRITE)
        } else {
            permissionManager.removePermission(asLong,sid, BasePermission.WRITE)
        }

    }
}
