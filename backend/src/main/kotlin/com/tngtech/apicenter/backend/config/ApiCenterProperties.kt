package com.tngtech.apicenter.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("apicenter")
class ApiCenterProperties{
    private var pageSize = 10
    private var anonymousUsername = "public"

    fun getPageSize(): Int = pageSize
    fun getAnonymousUsername(): String = anonymousUsername

    fun setPageSize(newPageSize: Int) {
        pageSize = newPageSize
    }
}