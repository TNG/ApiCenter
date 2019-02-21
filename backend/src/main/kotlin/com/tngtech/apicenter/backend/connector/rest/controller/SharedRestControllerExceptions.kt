package com.tngtech.apicenter.backend.connector.rest.controller

import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.http.HttpStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specification not found")
class HttpNotFoundException : RuntimeException()
