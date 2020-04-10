package com.tngtech.apicenter.service

import com.tngtech.apicenter.dto.InterfaceDto
import com.tngtech.apicenter.mapper.InterfaceMapper
import com.tngtech.apicenter.repository.InterfaceRepository
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID
import javax.persistence.EntityNotFoundException

@Service
class InterfaceService(private val interfaceMapper: InterfaceMapper, private val interfaceRepository: InterfaceRepository) {

    fun getInterfaces() = interfaceRepository.findAll().map { interfaceMapper.toDto(it) }

    fun createInterface(interfaceDto: InterfaceDto): InterfaceDto {
        val interfaceEntity = interfaceMapper.toEntity(interfaceDto)

        val createdInterface = interfaceRepository.save(interfaceEntity)
        return interfaceMapper.toDto(createdInterface)
    }

    fun updateInterface(interfaceId: UUID, interfaceDto: InterfaceDto): InterfaceDto {
        val interfaceDtoToStore = InterfaceDto(interfaceId, interfaceDto.name, interfaceDto.description, interfaceDto.type)

        val interfaceEntity = interfaceMapper.toEntity(interfaceDtoToStore)
        return interfaceMapper.toDto(interfaceEntity)
    }

    fun deleteInterface(interfaceId: UUID) {
        try {
            return interfaceRepository.deleteById(interfaceId)
        } catch (exception: EmptyResultDataAccessException) {
            throw EntityNotFoundException().initCause(exception)
        }
    }

    fun getInterface(interfaceId: UUID): InterfaceDto {
        val interfaceDto = interfaceRepository.findByIdOrNull(interfaceId) ?: throw EntityNotFoundException()

        return interfaceMapper.toDto(interfaceDto)
    }

}
