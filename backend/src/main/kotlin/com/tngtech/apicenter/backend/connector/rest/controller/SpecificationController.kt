package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionFileDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedSpecificationIdException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/specifications")
class SpecificationController @Autowired constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val synchronizationService: SynchronizationService,
    private val versionFileDtoMapper: VersionFileDtoMapper,
    private val specificationDtoMapper: SpecificationDtoMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody versionFileDto: VersionFileDto): VersionDto {
        val version = versionFileDtoMapper.toDomain(versionFileDto)

        specificationPersistenceService.saveOne(version, version.metadata.id, version.metadata.endpointUrl)

        return versionFileDtoMapper.fromDomain(version)
    }

    @PutMapping("/{specificationIdFromPath}")
    fun updateSpecification(@RequestBody versionFileDto: VersionFileDto, @PathVariable specificationIdFromPath: String): VersionDto {
        val specificationId = getConsistentId(versionFileDto, specificationIdFromPath)

        val version = versionFileDtoMapper.toDomain(
            VersionFileDto(
                versionFileDto.fileContent,
                versionFileDto.fileUrl,
                versionFileDto.metaData,
                specificationId
            )
        )

        specificationPersistenceService.saveOne(version, ServiceId(specificationId), versionFileDto.fileUrl)

        return versionFileDtoMapper.fromDomain(version)
    }

    private fun getConsistentId(versionFileDto: VersionFileDto, specificationIdFromPath: String): String {
        versionFileDto.id?.let {
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