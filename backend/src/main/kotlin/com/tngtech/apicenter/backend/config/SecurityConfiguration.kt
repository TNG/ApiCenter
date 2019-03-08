package com.tngtech.apicenter.backend.config

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthorizationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletResponse
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import mu.KotlinLogging

val logger = KotlinLogging.logger {  }

@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecuritySecret: String

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/sessions").permitAll()

                // Angular route permissions
                // Should be available to unauthenticated users
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()

                // Angular resources required by anyone, for the unauthenticated to login
                .antMatchers("/3rdpartylicenses.txt").permitAll()
                .antMatchers("/favicon.ico").permitAll()
                .antMatchers("/index.html").permitAll()
                .antMatchers("/*.js*").permitAll()
                .antMatchers("/open-iconic.*").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(JwtAuthorizationFilter(authenticationManager(), jwtSecuritySecret))
                .addFilterAfter(AclFilter(jwtSecuritySecret), JwtAuthorizationFilter::class.java)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/resources/**")
    }
}

class AclFilter constructor(private val jwtSecret: String) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest,
                          response: ServletResponse,
                          chain: FilterChain
    ) {
        val req = request as HttpServletRequest
        val header = req.getHeader("Authorization")

        val user = JWT.require(Algorithm.HMAC512(jwtSecret.toByteArray()))
                .build()
                .verify(header.replace("Bearer ", ""))
                .subject

        logger.info(user)
        // GET Spec -> this user has read permission
        // POST, PUT, DELETE Spec -> this user has write permission

        chain.doFilter(request, response)
    }
}