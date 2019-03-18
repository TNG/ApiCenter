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
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedSpecificationIdException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.Long.parseLong
import java.lang.NumberFormatException
import java.util.*

@RestController
@RequestMapping("/api/v1/specifications")
class SpecificationController @Autowired constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val synchronizationService: SynchronizationService,
    private val specificationDtoMapper: SpecificationDtoMapper,
    private val specificationPermissionManager: SpecificationPermissionManager
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationDtoMapper.toDomain(specificationFileDto)

        specificationPersistenceService.save(specification)
        val currentlyAuthenticatedUser = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val id = specificationFileDto.id ?: UUID.randomUUID().toString()
        val asLong = try { parseLong(id) } catch (exc: NumberFormatException) { throw GiveUpOnAclException() }
        specificationPermissionManager.addPermission(asLong, PrincipalSid(currentlyAuthenticatedUser.userId), BasePermission.READ)
        specificationPermissionManager.addPermission(asLong, PrincipalSid(currentlyAuthenticatedUser.userId), BasePermission.WRITE)

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