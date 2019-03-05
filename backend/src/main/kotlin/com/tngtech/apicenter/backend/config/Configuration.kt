package com.tngtech.apicenter.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.cache.CacheManager
import org.springframework.cache.support.AbstractCacheManager
import org.springframework.cache.support.NoOpCacheManager
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Configuration {

    @Bean
    fun objectMapper() = ObjectMapper().registerModule(KotlinModule())

    @Bean
    fun yamlMapper() = YAMLMapper()

    @Bean
    fun cacheManager(): CacheManager {
        return NoOpCacheManager()
    }
}