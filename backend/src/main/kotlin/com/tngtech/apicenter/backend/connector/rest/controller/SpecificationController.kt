package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.handler.SpecificationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/specifications")
class SpecificationController @Autowired constructor(
    private val specificationHandler: SpecificationHandler,
    private val synchronizationService: SynchronizationService,
    private val specificationMapper: SpecificationMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationMapper.toDomain(specificationFileDto)

        specificationHandler.store(specification)

        return specificationMapper.fromDomain(specification)
    }

    @PutMapping("/{specificationId}")
    fun updateSpecification(@RequestBody specificationFileDto: SpecificationFileDto, @PathVariable specificationId: String): SpecificationDto {

        val specification = specificationMapper.toDomain(
            SpecificationFileDto(
                specificationFileDto.fileContent,
                specificationFileDto.fileUrl,
                UUID.fromString(specificationId)
            )
        )

        specificationHandler.store(specification)

        return specificationMapper.fromDomain(specification)
    }

    @GetMapping
    fun findAllSpecifications(): List<SpecificationDto> =
        specificationHandler.findAll().map { spec -> specificationMapper.fromDomain(spec) }

    @GetMapping("/{specificationId}")
    fun findSpecification(@PathVariable specificationId: UUID): SpecificationDto =
        specificationMapper.fromDomain(specificationHandler.findOne(specificationId)!!)

    @DeleteMapping("/{specificationId}")
    fun deleteSpecification(@PathVariable specificationId: UUID) {
        specificationHandler.delete(specificationId)
    }

    @PostMapping("/{specificationId}/synchronize")
    fun synchronizeSpecification(@PathVariable specificationId: UUID) {
        synchronizationService.synchronize(specificationId)
    }

    @GetMapping("/search/{searchString}")
    fun searchSpecification(@PathVariable searchString: String): List<SpecificationDto> =
        specificationHandler.search(searchString).map { spec -> specificationMapper.fromDomain(spec) }
}