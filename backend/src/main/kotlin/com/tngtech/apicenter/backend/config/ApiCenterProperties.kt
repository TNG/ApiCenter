package com.tngtech.apicenter.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("apicenter")
class ApiCenterProperties{
    private var pageSize = 10

    fun getPageSize(): Int = pageSize

    fun setPageSize(newPageSize: Int) {
        pageSize = newPageSize
    }
}