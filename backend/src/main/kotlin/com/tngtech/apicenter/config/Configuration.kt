package com.tngtech.apicenter.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class Configuration {

    @Bean
    fun modelMapper() = ModelMapper()
}
