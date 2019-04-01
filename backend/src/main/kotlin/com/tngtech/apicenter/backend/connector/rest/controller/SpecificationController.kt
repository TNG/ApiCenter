package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationToken
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.GiveUpOnAclException
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedSpecificationIdException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.NumberFormatException
import mu.KotlinLogging
import org.springframework.security.acls.model.Permission

private val logger = KotlinLogging.logger {  }

@RestController
@RequestMapping("/api/v1/specifications")
class SpecificationController @Autowired constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val synchronizationService: SynchronizationService,
    private val specificationDtoMapper: SpecificationDtoMapper,
    private val specificationPermissionManager: SpecificationPermissionManager,
    private val permissionManager: SpecificationPermissionManager
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationDtoMapper.toDomain(specificationFileDto)

        specificationPersistenceService.save(specification)
        val currentlyAuthenticatedUser = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken

        try {
            val idAsLong = specification.id.id.toLong()
            specificationPermissionManager.addPermission(idAsLong, PrincipalSid(currentlyAuthenticatedUser.userId), BasePermission.READ)
            specificationPermissionManager.addPermission(idAsLong, PrincipalSid(currentlyAuthenticatedUser.userId), BasePermission.WRITE)
        } catch (exc: NumberFormatException) {
            logger.info(exc.toString())
            throw GiveUpOnAclException()
        }

        return specificationDtoMapper.fromDomain(specification)
    }

    @PutMapping("/{specificationIdFromPath}")
    fun updateSpecification(@RequestBody specificationFileDto: SpecificationFileDto, @PathVariable specificationIdFromPath: String): SpecificationDto {
        val specificationId = getConsistentId(specificationFileDto, specificationIdFromPath)

        val specification = specificationDtoMapper.toDomain(
            SpecificationFileDto(
                specificationFileDto.fileContent,
                specificationFileDto.fileUrl,
                specificationFileDto.metaData,
                specificationId
            )
        )

        specificationPersistenceService.save(specification)

        return specificationDtoMapper.fromDomain(specification)
    }

    private fun getConsistentId(specificationFileDto: SpecificationFileDto, specificationIdFromPath: String): String {
        specificationFileDto.id?.let {
            idFromFile -> if (idFromFile != specificationIdFromPath)
                throw MismatchedSpecificationIdException(idFromFile, specificationIdFromPath)
        }
        return specificationIdFromPath
    }

    @PutMapping("/api/v1/specifications/{specificationId}/chmod")
    fun chmodVersion(@PathVariable specificationId: String,
                     @RequestParam(value = "read", required = false) grantRead: Boolean,
                     @RequestParam(value = "write", required = false) grantWrite: Boolean
    ) {
        // TODO: Return 400 when principal doesn't exist (should be one of user, group, or 'all', 'everyone' keyword)

        val currentlyAuthenticatedUser = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val sid = PrincipalSid(currentlyAuthenticatedUser.userId)

        try {
            val idAsLong = specificationId.toLong()
            // exceptions may be thrown when attempting modifications, which are not yet handled
            changePermission(idAsLong, grantRead, sid, BasePermission.READ)
            changePermission(idAsLong, grantWrite, sid, BasePermission.WRITE)

        } catch (exc: NumberFormatException) {
            logger.info(exc.toString())
            throw GiveUpOnAclException()
        }

    }

    private fun changePermission(specificationId: Long, grant: Boolean, sid: PrincipalSid, permission: Permission) {
        if (grant)
            permissionManager.addPermission(specificationId, sid, permission)
        else
            permissionManager.removePermission(specificationId, sid, permission)
    }

    @GetMapping
    fun findAllSpecifications(): List<SpecificationDto> =
        specificationPersistenceService.findAll().map { spec -> specificationDtoMapper.fromDomain(spec) }

    @GetMapping("/{specificationId}")
    fun findSpecification(@PathVariable specificationId: String): SpecificationDto {
        val specification = specificationPersistenceService.findOne(ServiceId(specificationId))
        return specification?.let { specificationDtoMapper.fromDomain(it) } ?:
            throw SpecificationNotFoundException(specificationId)
    }

    @DeleteMapping("/{specificationId}")
    fun deleteSpecification(@PathVariable specificationId: String) {
        specificationPersistenceService.delete(ServiceId(specificationId))
    }

    @PostMapping("/{specificationId}/synchronize")
    fun synchronizeSpecification(@PathVariable specificationId: String) {
        synchronizationService.synchronize(ServiceId(specificationId))
    }

    @GetMapping("/search/{searchString}")
    fun searchSpecification(@PathVariable searchString: String): List<SpecificationDto> =
        specificationPersistenceService.search(searchString).map { spec -> specificationDtoMapper.fromDomain(spec) }
}