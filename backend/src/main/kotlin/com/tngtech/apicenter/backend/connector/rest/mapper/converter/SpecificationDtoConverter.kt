package com.tngtech.apicenter.backend.connector.rest.mapper.converter

import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationDto
import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationToken
import com.tngtech.apicenter.backend.domain.entity.Specification
import ma.glasnost.orika.CustomConverter
import ma.glasnost.orika.MappingContext
import ma.glasnost.orika.metadata.Type
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class SpecificationDtoConverter constructor(
        private val permissionManager: SpecificationPermissionManager,
        private val jwtAuthenticationProvider: JwtAuthenticationProvider
) : CustomConverter<Specification, SpecificationDto>() {

    override fun convert(source: Specification, destinationType: Type<out SpecificationDto>?, mappingContext: MappingContext?): SpecificationDto {

        val showEditButtons = try {
            val currentUserSid = jwtAuthenticationProvider.getCurrentPrincipal()
            permissionManager.hasPermission(source.id.id.toLong(), currentUserSid, BasePermission.WRITE)
        } catch (exc: NumberFormatException) {
            true
        }

        return SpecificationDto(
                source.id.id,
                source.title,
                source.description,
                source.versions.map { version-> mapperFacade.map(version, VersionDto::class.java) },
                source.remoteAddress,
                showEditButtons
        )
    }
}
