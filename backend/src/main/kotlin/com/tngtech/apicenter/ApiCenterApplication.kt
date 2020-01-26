package com.tngtech.apicenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApiCenterApplication

fun main(args: Array<String>) {
    runApplication<ApiCenterApplication>(*args)
}
