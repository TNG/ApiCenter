package com.tngtech.apicenter.backend.config

import com.atlassian.crowd.service.client.CrowdClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.factory.annotation.Autowired
import com.tngtech.apicenter.backend.connector.crowd.service.CrowdAuthenticationService
import com.tngtech.apicenter.backend.connector.crowd.service.NullAuthenticationService
import com.tngtech.apicenter.backend.domain.service.ExternalAuthenticationService

@Configuration
class Configuration @Autowired constructor(private val crowdClient: CrowdClient) {

    @Bean
    fun objectMapper() = ObjectMapper().registerModule(KotlinModule())

    @Bean
    fun yamlMapper() = YAMLMapper()

    @Value("\${auth.service}")
    private lateinit var authServiceProperty: String;

    @Bean
    fun externalAuthenticationService() : ExternalAuthenticationService {
        return when (authServiceProperty) {
            "crowd" -> CrowdAuthenticationService(crowdClient);
            else -> {
                NullAuthenticationService();
            }
        }
    }
}