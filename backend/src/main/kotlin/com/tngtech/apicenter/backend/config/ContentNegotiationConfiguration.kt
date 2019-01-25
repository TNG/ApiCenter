package com.tngtech.apicenter.backend.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.http.MediaType

@Configuration
class WebConfig : WebMvcConfigurer {

  override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
    configurer.
            ignoreAcceptHeader(true).
            defaultContentType(MediaType.APPLICATION_JSON).
            mediaType("yml", MediaType.valueOf("application/yml")).
            mediaType("json", MediaType.APPLICATION_JSON);
  }
}