package com.tngtech.apicenter.mapper

import com.tngtech.apicenter.dto.InterfaceDto
import com.tngtech.apicenter.dto.InterfaceTypeDto
import com.tngtech.apicenter.entity.InterfaceEntity
import com.tngtech.apicenter.entity.InterfaceTypeEntity
import org.springframework.stereotype.Component

@Component
class InterfaceMapper {

    fun toEntity(interfaceDto: InterfaceDto) = InterfaceEntity(interfaceDto.id, interfaceDto.name, interfaceDto.description, toInterfaceTypeEntity(interfaceDto.type))

    fun toDto(interfaceEntity: InterfaceEntity) = InterfaceDto(interfaceEntity.id, interfaceEntity.name, interfaceEntity.description, toInterfaceTypeDto(interfaceEntity.type))

    private fun toInterfaceTypeEntity(interfaceTypeDto: InterfaceTypeDto) = InterfaceTypeEntity.valueOf(interfaceTypeDto.toString())

    private fun toInterfaceTypeDto(interfaceTypeEntity: InterfaceTypeEntity) = InterfaceTypeDto.valueOf(interfaceTypeEntity.toString())

}
