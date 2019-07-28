package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.config.ApiCenterProperties
import com.tngtech.apicenter.backend.connector.rest.dto.ResultPageDto
import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.connector.rest.mapper.ServiceDtoMapper
import com.tngtech.apicenter.backend.connector.rest.mapper.SpecificationFileDtoMapper
import com.tngtech.apicenter.backend.domain.entity.Role
import com.tngtech.apicenter.backend.domain.exceptions.SpecificationNotFoundException
import com.tngtech.apicenter.backend.domain.entity.ServiceId
import com.tngtech.apicenter.backend.domain.entity.Specification
import com.tngtech.apicenter.backend.domain.exceptions.BadUrlException
import com.tngtech.apicenter.backend.domain.exceptions.MismatchedServiceIdException
import com.tngtech.apicenter.backend.domain.handler.ServiceHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("/api/v1/service")
class ServiceController @Autowired constructor(
        private val apiCenterProperties: ApiCenterProperties,
        private val serviceHandler: ServiceHandler,
        private val specificationFileDtoMapper: SpecificationFileDtoMapper,
        private val serviceDtoMapper: ServiceDtoMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun uploadSpecifications(@RequestBody specificationFileDtos: List<SpecificationFileDto>): List<Specification> {
        return specificationFileDtos.map { dto ->
            val specification = specificationFileDtoMapper.toDomain(dto)
            serviceHandler.addNewSpecification(specification, specification.metadata.id, dto.fileUrl, dto.isPublic)
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
                specificationId,
                specificationFileDto.isPublic
            )
        )

        serviceHandler.addNewSpecification(specification, ServiceId(specificationId), specificationFileDto.fileUrl, specificationFileDto.isPublic)

        return specificationFileDtoMapper.fromDomain(specification)
    }

    @PutMapping("/{serviceId}/permissions/{username}")
    fun changeRoleForService(@PathVariable serviceId: String,
                             @PathVariable username: String,
                             @RequestParam(value = "role") role: String
    ) {
        val id = ServiceId(serviceId)
        try {
            serviceHandler.assignRole(id, username, Role.valueOf(role))
        } catch (exception: IllegalArgumentException) {
            throw BadUrlException(role)
        }
    }

    @DeleteMapping("/{serviceId}/permissions/{username}")
    fun deleteRoleForService(@PathVariable serviceId: String,
                             @PathVariable username: String
    ) {
        val id = ServiceId(serviceId)
        serviceHandler.removeRole(id, username)
    }

    @GetMapping("/{serviceId}/permissions/{username}")
    fun getRoleForService(@PathVariable serviceId: String,
                          @PathVariable username: String
    ): Role? {
        val id = ServiceId(serviceId)
        return serviceHandler.getRole(id, username)
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
            val resultPage = serviceHandler.findAll(page.toInt(), apiCenterProperties.getPageSize()).map { service -> serviceDtoMapper.fromDomain(service) }
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
        serviceHandler.synchroniseRemoteService(ServiceId(serviceId))
    }

    @GetMapping("/search/{searchString}")
    fun searchForServices(@PathVariable searchString: String): List<ServiceDto> =
        serviceHandler.search(searchString).map { spec -> serviceDtoMapper.fromDomain(spec) }

    @GetMapping("/search/")
    fun searchWithBlank(): List<ServiceDto> = listOf()
}