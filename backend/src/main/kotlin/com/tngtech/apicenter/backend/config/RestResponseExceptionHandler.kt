package com.tngtech.apicenter.backend.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import java.time.format.DateTimeFormatter

@ControllerAdvice
class RestResponseExceptionHandler() {

    @ExceptionHandler(SpecificationNotFoundException::class)
    fun handleNotFound(): ResponseEntity<ErrorMessage> {
        return ResponseEntity(
                ErrorMessage("Specification not found"),
                null,
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(SpecificationParseFailureException::class)
    fun handleBadRequest(exc: SpecificationParseFailureException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(
                ErrorMessage(exc.msg),
                null,
                HttpStatus.BAD_REQUEST
        )
    }

    @ExceptionHandler(VersionAlreadyExistsException::class)
    fun handleVersionAlreadyExists(exc: VersionAlreadyExistsException): ResponseEntity<ErrorMessage> {
        return ResponseEntity(
                ErrorMessage("A specification with the same version already exists for ${exc.specificationTitle}"),
                null,
                HttpStatus.UNPROCESSABLE_ENTITY
        )
    }
}

class SpecificationNotFoundException : RuntimeException()
class SpecificationParseFailureException(val msg: String) : RuntimeException()
class VersionAlreadyExistsException(val specificationTitle: String) : RuntimeException()

data class ErrorMessage(
        val message: String,
        val timestamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
)
