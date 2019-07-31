package com.tngtech.apicenter.backend.config

import com.tngtech.apicenter.backend.domain.exceptions.*
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.Instant
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {  }

data class ErrorMessage(
        val httpReasonPhrase: String,
        val userMessage: String,
        val timestamp: String = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
)

fun makeResponseEntity(userMessage: String, status: HttpStatus): ResponseEntity<ErrorMessage> {
    return ResponseEntity(
        ErrorMessage(status.reasonPhrase, userMessage),
        null,
        status
    )
}

@ControllerAdvice
class RestResponseExceptionHandler {

    @ExceptionHandler(SpecificationNotFoundException::class)
    fun handleNotFound(exception: SpecificationNotFoundException): ResponseEntity<ErrorMessage> {
        logger.info("Specification ${exception.serviceId} ${exception.version} not found", exception)
        return makeResponseEntity("Specification not found", HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(SpecificationParseException::class)
    fun handleBadRequest(exception: SpecificationParseException) =
            makeResponseEntity(exception.userMessage, HttpStatus.BAD_REQUEST)

    @ExceptionHandler(SpecificationAlreadyExistsException::class)
    fun handleVersionAlreadyExists(exception: SpecificationAlreadyExistsException) =
            makeResponseEntity("A specification with the same version already exists for ${exception.title}", HttpStatus.CONFLICT)

    @ExceptionHandler(InvalidServiceIdException::class)
    fun handleUnacceptableId(exception: InvalidServiceIdException) =
            makeResponseEntity("The API ID supplied (${exception.userDefinedId}) should only contain numbers, " +
                    "A-Z characters, underscores and hyphens", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(MismatchedServiceIdException::class)
    fun handleServiceIdMismatch(exception: MismatchedServiceIdException) =
            makeResponseEntity("The API ID used in the upload URL (${exception.urlPathId}) " +
                    "is not the same as the API ID used in the specification body (${exception.userDefinedId})", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(SpecificationDuplicationException::class)
    fun handleSpecificationDuplication(exception: SpecificationDuplicationException) =
            makeResponseEntity("This specification already exists on ApiCenter", HttpStatus.ACCEPTED)

    @ExceptionHandler(SpecificationConflictException::class)
    fun handleSpecificationConflict(exception: SpecificationConflictException) =
            makeResponseEntity("There is already a different specification with the same version. " +
                    "Please solve this conflict, for example by increasing the version.", HttpStatus.CONFLICT)

    @ExceptionHandler(RemoteFileConnectionRefusedException::class)
    fun handleConnectionRefused(exception: RemoteFileConnectionRefusedException) =
            makeResponseEntity("Specification couldn't be retrieved (HTTP Not found)", HttpStatus.NOT_FOUND)

    @ExceptionHandler(BadUrlException::class)
    fun handleMalformedUrl(exception: BadUrlException) =
            makeResponseEntity("URL couldn't be parsed", HttpStatus.BAD_REQUEST)

    @ExceptionHandler(PermissionDeniedException::class)
    fun handlePermissionDenied(exception: PermissionDeniedException) =
            makeResponseEntity("You don't have edit permission on specification ${exception.serviceId}", HttpStatus.FORBIDDEN)

    @ExceptionHandler(NotEnoughEditorsException::class)
    fun handleNotEnoughEditors(exception: NotEnoughEditorsException) =
            makeResponseEntity("A service needs at least one associated user with edit permission", HttpStatus.FORBIDDEN)

    @ExceptionHandler(UserDoesntExistException::class)
    fun handleUserDoesntExist(exception: UserDoesntExistException) =
            makeResponseEntity("User ${exception.username} doesn't exist", HttpStatus.BAD_REQUEST)
}

