package com.tngtech.apicenter.controller

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.service.ApplicationService
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

@RestController
@RequestMapping("/api/applications")
class ApplicationController(private val applicationService: ApplicationService) {

    @GetMapping
    fun getApplications() = applicationService.getApplications()

    // TODO /api/applications/{applicationId}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createApplication(@RequestBody applicationDto: ApplicationDto) = applicationService.createApplication(applicationDto)

    @PutMapping("/{applicationId}")
    fun updateApplication(@PathVariable applicationId: String, @RequestBody applicationDto: ApplicationDto) = applicationService.updateApplication(applicationId, applicationDto)

    @DeleteMapping("/{applicationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteApplication(@PathVariable applicationId: String) = applicationService.deleteApplication(applicationId)
}