package com.tngtech.apicenter.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ApiCenterProperties::class)
class Configuration {

    @Bean
    fun objectMapper() = ObjectMapper().registerModule(KotlinModule())

    @Bean
    fun yamlMapper() = YAMLMapper()
}