package com.tngtech.apicenter.backend.config

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

@EnableWebSecurity
@Order(Ordered.HIGHEST_PRECEDENCE)
@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecuritySecret: String;

    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
            .cors()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/sessions").permitAll()

            .antMatchers("/").permitAll()
            .antMatchers("/3rdpartylicenses.txt").permitAll()
            .antMatchers("/favicon.ico").permitAll()
            .antMatchers("/index.html").permitAll()
            .antMatchers("/*.js*").permitAll()
            .antMatchers("/open-iconic.*").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilter(JwtAuthorizationFilter(authenticationManager(), jwtSecuritySecret))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/resources/**")
    }
}

