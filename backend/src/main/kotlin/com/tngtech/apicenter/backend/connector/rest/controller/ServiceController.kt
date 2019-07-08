package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.ResultPageDto
import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.ServiceDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.connector.rest.service.RemoteServiceUpdater
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.BadUrlException
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
    fun uploadSpecifications(@RequestBody specificationFileDtos: List<SpecificationFileDto>): List<Specification> {
        return specificationFileDtos.map { dto ->
            val specification = specificationFileDtoMapper.toDomain(dto)
            serviceHandler.addNewSpecification(specification, specification.metadata.id, dto.fileUrl)
            specification
        }
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

    @GetMapping(params = ["page"])
    fun findAllServices(@RequestParam(value = "page") page: String): ResultPageDto<ServiceDto> =
        try {
            val resultPage = serviceHandler.findAll(page.toInt(), 10).map { service -> serviceDtoMapper.fromDomain(service) }
            ResultPageDto(resultPage.content, resultPage.last)
        } catch (exception: NumberFormatException) {
            throw BadUrlException(page)
        }

    @GetMapping("/{serviceId}")
    fun findService(@PathVariable serviceId: String): ServiceDto {
        val specification = serviceHandler.findOne(ServiceId(serviceId))
        return specification?.let { serviceDtoMapper.fromDomain(it) } ?:
            throw SpecificationNotFoundException(serviceId)
    }

    @DeleteMapping("/{serviceId}")
    fun deleteService(@PathVariable serviceId: String) {
        serviceHandler.delete(ServiceId(serviceId))
    }

    @PostMapping("/{serviceId}/synchronize")
    fun synchronizeService(@PathVariable serviceId: String) {
        remoteServiceUpdater.synchronize(ServiceId(serviceId))
    }

    @GetMapping("/search/{searchString}")
    fun searchForServices(@PathVariable searchString: String): List<ServiceDto> =
        serviceHandler.search(searchString).map { spec -> serviceDtoMapper.fromDomain(spec) }

    @GetMapping("/search/")
    fun searchWithBlank(): List<ServiceDto> = listOf()
}