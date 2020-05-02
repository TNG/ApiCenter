package com.tngtech.apicenter.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {

    override fun addViewControllers(registry: ViewControllerRegistry) {
        with(registry) {
            addViewController("/applications").setViewName("forward:/index.html")
            addViewController("/interfaces").setViewName("forward:/index.html")
        }
    }
}
