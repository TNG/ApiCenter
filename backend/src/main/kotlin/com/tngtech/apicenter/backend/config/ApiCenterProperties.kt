package com.tngtech.apicenter.backend.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("apicenter")
class ApiCenterProperties{
    val pageSize = 10
}