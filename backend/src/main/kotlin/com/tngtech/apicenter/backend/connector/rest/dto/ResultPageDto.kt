package com.tngtech.apicenter.backend.connector.rest.dto

data class ResultPageDto<T>(
        val content: List<T>,
        val last: Boolean
)
