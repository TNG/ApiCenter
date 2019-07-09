package com.tngtech.apicenter.backend.connector.rest.dto

data class ResultPageDto<T> constructor(
        val content: List<T>,
        val last: Boolean
)
