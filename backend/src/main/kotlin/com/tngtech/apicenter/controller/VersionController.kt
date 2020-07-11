package com.tngtech.apicenter.controller

import com.tngtech.apicenter.dto.VersionFileDto
import com.tngtech.apicenter.service.VersionService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/versions")
class VersionController(private val versionService: VersionService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createVersion(@RequestBody versionDto: VersionFileDto) = versionService.createVersion(versionDto)

    @GetMapping
    fun getVersions() = versionService.getVersions()
}
