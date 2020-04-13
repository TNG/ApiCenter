package com.tngtech.apicenter.controller

import com.tngtech.apicenter.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun entityNotFoundExceptionHandler(entityNotFoundException: EntityNotFoundException) = ErrorResponse("Entity not found")
}
