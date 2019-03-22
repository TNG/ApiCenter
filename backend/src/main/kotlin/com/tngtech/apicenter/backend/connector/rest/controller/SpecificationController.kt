package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.service.SpecificationPersistenceService
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationUploadUrlMismatch
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/specifications")
class SpecificationController @Autowired constructor(
    private val specificationPersistenceService: SpecificationPersistenceService,
    private val synchronizationService: SynchronizationService,
    private val specificationDtoMapper: SpecificationDtoMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationDtoMapper.toDomain(specificationFileDto)

        specificationPersistenceService.save(specification)

        return specificationDtoMapper.fromDomain(specification)
    }

    @PutMapping("/{urlPathId}")
    fun updateSpecification(@RequestBody specificationFileDto: SpecificationFileDto, @PathVariable urlPathId: String): SpecificationDto {

        val specificationId = specificationFileDto.id?.let {
            userId -> if (userId == urlPathId) urlPathId
                      else throw SpecificationUploadUrlMismatch(userId, urlPathId)
        } ?: run {
            urlPathId
        }

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

    @GetMapping
    fun findAllSpecifications(): List<SpecificationDto> =
        specificationPersistenceService.findAll().map { spec -> specificationDtoMapper.fromDomain(spec) }

    @GetMapping("/{specificationId}")
    fun findSpecification(@PathVariable specificationId: String): SpecificationDto {
        val specification = specificationPersistenceService.findOne(specificationId)
        return specification?.let { specificationDtoMapper.fromDomain(it) } ?:
            throw SpecificationNotFoundException(specificationId)
    }

    @DeleteMapping("/{specificationId}")
    fun deleteSpecification(@PathVariable specificationId: String) {
        specificationPersistenceService.delete(specificationId)
    }

    @PostMapping("/{specificationId}/synchronize")
    fun synchronizeSpecification(@PathVariable specificationId: String) {
        synchronizationService.synchronize(specificationId)
    }

    @GetMapping("/search/{searchString}")
    fun searchSpecification(@PathVariable searchString: String): List<SpecificationDto> =
        specificationPersistenceService.search(searchString).map { spec -> specificationDtoMapper.fromDomain(spec) }
}