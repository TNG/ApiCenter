package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationMapper
import com.tngtech.apicenter.backend.connector.rest.service.SynchronizationService
import com.tngtech.apicenter.backend.domain.handler.SpecificationHandler
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
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
@Api(description = "Operations to create, update and maintain specifications")
class SpecificationController @Autowired constructor(
    private val specificationHandler: SpecificationHandler,
    private val synchronizationService: SynchronizationService,
    private val specificationMapper: SpecificationMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Upload a new specification, or define a remote specification by its url")
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationMapper.toDomain(specificationFileDto)

        specificationHandler.store(specification)

        return specificationMapper.fromDomain(specification)
    }

    @PutMapping("/{specificationId}")
    @ApiOperation("Update an existing specification")
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
    @ApiOperation("List all existing specifications")
    fun findAllSpecifications(): List<SpecificationDto> =
        specificationHandler.findAll().map { spec -> specificationMapper.fromDomain(spec) }

    @GetMapping("/{specificationId}")
    @ApiOperation("Find a specific specification by id")
    fun findSpecification(@PathVariable specificationId: UUID): SpecificationDto =
        specificationMapper.fromDomain(specificationHandler.findOne(specificationId)!!)

    @DeleteMapping("/{specificationId}")
    @ApiOperation("Delete a specification by id")
    fun deleteSpecification(@PathVariable specificationId: UUID) {
        specificationHandler.delete(specificationId)
    }

    @PostMapping("/{specificationId}/synchronize")
    @ApiOperation("Synchronize an existing specification with its remote source")
    fun synchronizeSpecification(@PathVariable specificationId: UUID) {
        synchronizationService.synchronize(specificationId)
    }

    @GetMapping("/search/{searchString}")
    @ApiOperation("Search for specifications by a search string")
    fun searchSpecification(@PathVariable searchString: String): List<SpecificationDto> =
        specificationHandler.search(searchString).map { spec -> specificationMapper.fromDomain(spec) }
}