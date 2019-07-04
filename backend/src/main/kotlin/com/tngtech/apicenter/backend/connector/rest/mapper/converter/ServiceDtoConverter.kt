package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.rest.dto.ServiceDto
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.domain.entity.PermissionType
import com.tngtech.apicenter.backend.domain.entity.Service
import com.tngtech.apicenter.backend.domain.service.PermissionsManager
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.stereotype.Component

@Component
class ServiceDtoConverter constructor(
        private val permissionsManager: PermissionsManager,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider
) : CustomConverter<Service, ServiceDto>() {

    override fun convert(source: Service, destinationType: Type<out ServiceDto>?, mappingContext: MappingContext?): ServiceDto {
        val userId = jwtAuthenticationProvider.getCurrentUser()
        val canEdit = permissionsManager.hasPermission(userId, source.id, PermissionType.EDIT)

        return ServiceDto(
                source.id.id,
                source.title,
                source.description,
                source.specifications.map { spec -> mapperFacade.map(spec, SpecificationDto::class.java) },
                source.remoteAddress,
                canEdit
        )
    }
}