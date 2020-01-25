package com.tngtech.apicenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ApicenterApplication

fun main(args: Array<String>) {
    runApplication<ApicenterApplication>(*args)
}
