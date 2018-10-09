package com.tngtech.apicenter.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@Configuration
@EnableSwagger2
class WebConfiguration {

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(corsRegistry: CorsRegistry) {
                corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
                    .allowedHeaders("*")
            }
        }
    }

    @Bean
    fun docket(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiMetadata())

    private fun apiMetadata() = ApiInfoBuilder()
        .title("ApiCenter REST API")
        .description("REST API for ApiCenter - a repository for all your OpenAPI specifications")
        .license("Apache License Version 2.0, January 2004")
        .build()
}