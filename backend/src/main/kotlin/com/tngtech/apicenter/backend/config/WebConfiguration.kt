package com.tngtech.apicenter.backend.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfiguration {

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(corsRegistry: CorsRegistry) {
                corsRegistry.addMapping("/**").allowedOrigins("*").allowedMethods("*")
                    .allowedHeaders("*")
            }

            override fun addViewControllers(registry: ViewControllerRegistry) {
                // Redirect to Angular's router
                registry.addViewController("/specifications/*/*").setViewName("forward:/index.html")
                registry.addViewController("/add-specifications").setViewName("forward:/index.html")
                registry.addViewController("/edit-specifications/**").setViewName("forward:/index.html")
                registry.addViewController("/search").setViewName("forward:/index.html")
                registry.addViewController("/search/**").setViewName("forward:/index.html")
                registry.addViewController("/login").setViewName("forward:/index.html")
            }
        }
    }
}