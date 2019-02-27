package com.tngtech.apicenter.backend.config

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import java.time.format.DateTimeFormatter
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {  }
class SpecificationNotFoundException(val specificationId: UUID, val version: String? = "") : RuntimeException()
class SpecificationParseException(val msg: String) : RuntimeException()
class VersionAlreadyExistsException(val specificationTitle: String) : RuntimeException()

data class ErrorMessage(
        val message: String,
        val timestamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
)

@ControllerAdvice
class RestResponseExceptionHandler {

    @ExceptionHandler(SpecificationNotFoundException::class)
    fun handleNotFound(exc: SpecificationNotFoundException): ResponseEntity<ErrorMessage> {
        logger.info("Specification ${exc.specificationId} ${exc.version} not found")
        return ResponseEntity(
                ErrorMessage("Specification not found"),
                null,
                HttpStatus.NOT_FOUND
        )
    }

    @ExceptionHandler(SpecificationParseException::class)
    fun handleBadRequest(exc: SpecificationParseException): ResponseEntity<ErrorMessage> {
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
                HttpStatus.CONFLICT
        )
    }
}

