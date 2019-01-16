package com.tngtech.apicenter.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.http.MediaType

@Configuration
class WebConfig : WebMvcConfigurerAdapter() {

  override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
    configurer.
            parameterName("mediaType").
            ignoreAcceptHeader(true).
            defaultContentType(MediaType.APPLICATION_JSON).
            mediaType("yml", MediaType.valueOf("application/yml")).
            mediaType("json", MediaType.APPLICATION_JSON);
  }
}