package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.IdNotCastableToLongException
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedSpecificationIdException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
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
    private val permissionManager: SpecificationPermissionManager,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationDtoMapper.toDomain(specificationFileDto)

        specificationPersistenceService.save(specification)

        try {
            val currentUserSid = jwtAuthenticationProvider.getCurrentPrincipal()
            val idAsLong = specification.id.id.toLong()
            permissionManager.addPermission(idAsLong, currentUserSid, BasePermission.READ)
            permissionManager.addPermission(idAsLong, currentUserSid, BasePermission.WRITE)
        } catch (exc: NumberFormatException) {
            // Permissions cannot be set on this object, nothing to do
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

    @PutMapping("/{specificationId}/chmod/{userId}")
    fun chmodVersion(@PathVariable specificationId: String,
                     @PathVariable userId: String,
                     @RequestParam(value = "grantRead", defaultValue = "false") grantRead: String,
                     @RequestParam(value = "grantWrite", defaultValue = "false") grantWrite: String
    ) {
        logger.info(grantRead)
        logger.info(grantWrite)
        logger.info(specificationId)
        logger.info(userId)

        try {
            val idAsLong = specificationId.toLong()
            // TODO: Add exception if the target principal or specification doesn't exist
            val targetUserSid = PrincipalSid(userId)
            changePermission(idAsLong, grantRead.toBoolean(), targetUserSid, BasePermission.READ)
            changePermission(idAsLong, grantWrite.toBoolean(), targetUserSid, BasePermission.WRITE)

        } catch (exc: NumberFormatException) {
            throw IdNotCastableToLongException()
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
        specificationPersistenceService.findAll()
                .map { spec -> specificationDtoMapper.fromDomain(spec) }
                .filter { spec -> userHasPermission(spec.id, BasePermission.READ) }

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
        specificationPersistenceService.search(searchString)
                .map { spec -> specificationDtoMapper.fromDomain(spec) }
                .filter { spec -> userHasPermission(spec.id, BasePermission.READ) }

    private fun userHasPermission(specificationId: String, permission: Permission): Boolean {
        val currentUserSid = jwtAuthenticationProvider.getCurrentPrincipal()

        return try {
            permissionManager.hasPermission(specificationId.toLong(), currentUserSid, permission)
        } catch (exc: NumberFormatException) {
            true
        }
    }
}