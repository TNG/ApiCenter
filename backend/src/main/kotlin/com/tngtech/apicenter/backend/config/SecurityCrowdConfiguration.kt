package com.tngtech.apicenter.backend.config

import com.atlassian.crowd.integration.http.CrowdHttpAuthenticator
import com.atlassian.crowd.integration.http.CrowdHttpAuthenticatorImpl
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelper
import com.atlassian.crowd.integration.http.util.CrowdHttpTokenHelperImpl
import com.atlassian.crowd.integration.http.util.CrowdHttpValidationFactorExtractorImpl
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory
import com.atlassian.crowd.integration.springsecurity.RemoteCrowdAuthenticationProvider
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsService
import com.atlassian.crowd.integration.springsecurity.user.CrowdUserDetailsServiceImpl
import com.atlassian.crowd.service.client.ClientProperties
import com.atlassian.crowd.service.client.ClientPropertiesImpl
import com.atlassian.crowd.service.client.ClientResourceLocator
import com.atlassian.crowd.service.client.CrowdClient
import com.atlassian.crowd.service.factory.CrowdClientFactory
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.session.HttpSessionEventPublisher

@EnableWebSecurity
@Configuration
class SecurityCrowdConfiguration : WebSecurityConfigurerAdapter() {

    @Bean
    fun sessionRegistry() = SessionRegistryImpl()

    @Bean
    fun httpSessionEventPublisher() = ServletListenerRegistrationBean<HttpSessionEventPublisher>(
        HttpSessionEventPublisher()
    )

    @Bean
    fun resourceLocator() = ClientResourceLocator("crowd.properties")

    @Bean
    fun clientProperties(resourceLocator: ClientResourceLocator) =
        ClientPropertiesImpl.newInstanceFromResourceLocator(resourceLocator)

    @Bean
    fun crowdClientFactory() = RestCrowdClientFactory()

    @Bean
    fun crowdClient(crowdClientFactory: CrowdClientFactory, clientProperties: ClientProperties) =
        crowdClientFactory.newInstance(clientProperties)

    @Bean
    fun validationFactorExtractor() =
        CrowdHttpValidationFactorExtractorImpl.getInstance()

    @Bean
    fun tokenHelper() =
        CrowdHttpTokenHelperImpl.getInstance(validationFactorExtractor())

    @Bean
    fun crowdHttpAuthenticator(
        crowdClient: CrowdClient,
        clientProperties: ClientProperties,
        tokenHelper: CrowdHttpTokenHelper
    ) =
        CrowdHttpAuthenticatorImpl(crowdClient, clientProperties, tokenHelper)

    @Bean
    fun crowdUserDetailsService(crowdClient: CrowdClient): CrowdUserDetailsService {
        val crowdUserDetailsService = CrowdUserDetailsServiceImpl()
        crowdUserDetailsService.setCrowdClient(crowdClient)
        return crowdUserDetailsService
    }

    @Bean
    fun crowdAuthenticationProvider(
        crowdClient: CrowdClient,
        crowdHttpAuthenticator: CrowdHttpAuthenticator,
        crowdUserDetailsService: CrowdUserDetailsService
    ) = RemoteCrowdAuthenticationProvider(
        crowdClient, crowdHttpAuthenticator, crowdUserDetailsService
    )
}