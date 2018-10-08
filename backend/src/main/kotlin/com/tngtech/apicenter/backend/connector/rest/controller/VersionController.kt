package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.handler.VersionHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/versions")
class VersionController constructor(private val versionHandler: VersionHandler, private val versionDtoMapper: VersionDtoMapper) {

    @GetMapping("/{versionId}")
    fun findVersion(@PathVariable versionId: UUID): VersionDto = versionDtoMapper.fromDomain(versionHandler.findOne(versionId))

    @DeleteMapping("/{versionId}")
    fun deleteVersion(@PathVariable versionId: UUID) {
        versionHandler.delete(versionId)
    }
}