package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.InterfaceDto
import com.tngtech.apicenter.mapper.toDto
import com.tngtech.apicenter.mapper.toEntity
import com.tngtech.apicenter.repository.InterfaceRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
class InterfaceService(private val interfaceRepository: InterfaceRepository) {

    fun getInterfaces() = interfaceRepository.findAll().map { it.toDto() }

    fun createInterface(interfaceDto: InterfaceDto): InterfaceDto {
        val interfaceEntity = interfaceDto.toEntity()

        val createdInterface = interfaceRepository.save(interfaceEntity)
        return createdInterface.toDto()
    }

    fun updateInterface(interfaceId: UUID, interfaceDto: InterfaceDto): InterfaceDto {
        val interfaceDtoToStore = InterfaceDto(interfaceId, interfaceDto.name, interfaceDto.description, interfaceDto.type, interfaceDto.applicationId)

        val interfaceEntity = interfaceDtoToStore.toEntity()
        val updatedEntity = interfaceRepository.save(interfaceEntity)
        return updatedEntity.toDto()
    }

    fun deleteInterface(interfaceId: UUID) {
        try {
            return interfaceRepository.deleteById(interfaceId)
        } catch (exception: EmptyResultDataAccessException) {
            throw EntityNotFoundException().initCause(exception)
        }
    }

    fun getInterface(interfaceId: UUID): InterfaceDto {
        val interfaceEntity = interfaceRepository.findByIdOrNull(interfaceId) ?: throw EntityNotFoundException()

        return interfaceEntity.toDto()
    }

    fun getInterfacesForApplicationId(applicationId: UUID): List<InterfaceDto> =
        interfaceRepository.findByApplicationId(applicationId).map { it.toDto() }

}
