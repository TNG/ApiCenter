package com.tngtech.apicenter.controller

import com.tngtech.apicenter.dto.InterfaceDto
import com.tngtech.apicenter.service.InterfaceService
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
import javax.validation.Valid

@RestController
@RequestMapping("/api/interfaces")
class InterfaceController(private val interfaceService: InterfaceService) {

    @GetMapping
    fun getInterfaces() = interfaceService.getInterfaces()

    @GetMapping("/{interfaceId}")
    fun getInterface(@PathVariable interfaceId: UUID) = interfaceService.getInterface(interfaceId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createApplication(@Valid @RequestBody interfaceDto: InterfaceDto) = interfaceService.createInterface(interfaceDto)

    @PutMapping("/{interfaceId}")
    fun updateApplication(@PathVariable interfaceId: UUID, @Valid @RequestBody interfaceDto: InterfaceDto) = interfaceService.updateInterface(interfaceId, interfaceDto)

    @DeleteMapping("/{interfaceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteApplication(@PathVariable interfaceId: UUID) = interfaceService.deleteInterface(interfaceId)
}
