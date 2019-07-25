package com.tngtech.apicenter.backend.config

import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl
import com.atlassian.crowd.service.client.ClientPropertiesImpl
import com.atlassian.crowd.service.client.ClientResourceLocator
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.session.HttpSessionEventPublisher

@Configuration
@ConditionalOnProperty(name = ["auth.service"], havingValue = "crowd")
class SecurityCrowdConfiguration constructor(private val apiCenterProperties: ApiCenterProperties): WebSecurityConfigurerAdapter() {

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.authenticationProvider(jwtAuthenticationProvider())
    }

    @Bean
    fun sessionRegistry() = SessionRegistryImpl()

    @Bean
    fun httpSessionEventPublisher() =
        ServletListenerRegistrationBean<HttpSessionEventPublisher>(HttpSessionEventPublisher())

    @Bean
    fun resourceLocator() = ClientResourceLocator("crowd.properties")

    @Bean
    fun clientProperties() = ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator())

    @Bean
    fun crowdClientFactory() = RestCrowdClientFactory()

    @Bean
    fun crowdClient() = crowdClientFactory().newInstance(clientProperties())

    @Bean
    fun validationFactorExtractor() = CrowdHttpValidationFactorExtractorImpl.getInstance()

    @Bean
    fun tokenHelper() = CrowdHttpTokenHelperImpl.getInstance(validationFactorExtractor())

    @Bean
    fun crowdHttpAuthenticator() = CrowdHttpAuthenticatorImpl(crowdClient(), clientProperties(), tokenHelper())

    @Bean
    fun crowdUserDetailsService(): CrowdUserDetailsService {
        val cuds = CrowdUserDetailsServiceImpl();
        cuds.setCrowdClient(crowdClient());
        return cuds;
    }

    @Bean
    fun jwtAuthenticationProvider() = JwtAuthenticationProvider(apiCenterProperties)

    @Bean
    override fun authenticationManagerBean() = super.authenticationManagerBean()
}