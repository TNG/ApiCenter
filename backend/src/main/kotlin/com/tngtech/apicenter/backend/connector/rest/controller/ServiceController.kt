package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.ServiceDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.RemoteServiceUpdater
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedServiceIdException
import com.tngtech.apicenter.backend.domain.handler.ServiceHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/service")
class ServiceController @Autowired constructor(
        private val serviceHandler: ServiceHandler,
        private val remoteServiceUpdater: RemoteServiceUpdater,
        private val specificationFileDtoMapper: SpecificationFileDtoMapper,
        private val serviceDtoMapper: ServiceDtoMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecification(@RequestBody specificationFileDto: SpecificationFileDto): SpecificationDto {
        val specification = specificationFileDtoMapper.toDomain(specificationFileDto)

        serviceHandler.addNewSpecification(specification, specification.metadata.id, specification.metadata.endpointUrl)

        return specificationFileDtoMapper.fromDomain(specification)
    }

    @PutMapping("/{serviceIdFromPath}")
    fun updateSpecification(@RequestBody specificationFileDto: SpecificationFileDto, @PathVariable serviceIdFromPath: String): SpecificationDto {
        val specificationId = getConsistentId(specificationFileDto, serviceIdFromPath)

        val specification = specificationFileDtoMapper.toDomain(
            SpecificationFileDto(
                specificationFileDto.fileContent,
                specificationFileDto.fileUrl,
                specificationFileDto.metadata,
                specificationId
            )
        )

        serviceHandler.addNewSpecification(specification, ServiceId(specificationId), specificationFileDto.fileUrl)

        return specificationFileDtoMapper.fromDomain(specification)
    }

    private fun getConsistentId(specificationFileDto: SpecificationFileDto, specificationIdFromPath: String): String {
        specificationFileDto.id?.let {
            idFromFile -> if (idFromFile != specificationIdFromPath)
                throw MismatchedServiceIdException(idFromFile, specificationIdFromPath)
        }
        return specificationIdFromPath
    }

    @GetMapping
    fun findAllServices(): List<ServiceDto> =
        serviceHandler.findAll().map { service -> serviceDtoMapper.fromDomain(service) }

    @GetMapping("/{specificationId}")
    fun findService(@PathVariable specificationId: String): ServiceDto {
        val specification = serviceHandler.findOne(ServiceId(specificationId))
        return specification?.let { serviceDtoMapper.fromDomain(it) } ?:
            throw SpecificationNotFoundException(specificationId)
    }

    @DeleteMapping("/{specificationId}")
    fun deleteService(@PathVariable specificationId: String) {
        serviceHandler.delete(ServiceId(specificationId))
    }

    @PostMapping("/{specificationId}/synchronize")
    fun synchronizeService(@PathVariable specificationId: String) {
        remoteServiceUpdater.synchronize(ServiceId(specificationId))
    }

    @GetMapping("/search/{searchString}")
    fun searchForServices(@PathVariable searchString: String): List<ServiceDto> =
        serviceHandler.search(searchString).map { spec -> serviceDtoMapper.fromDomain(spec) }
}