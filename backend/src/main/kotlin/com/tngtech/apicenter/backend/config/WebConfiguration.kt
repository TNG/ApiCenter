package com.tngtech.apicenter.backend.config

import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import com.tngtech.apicenter.backend.domain.exceptions.GiveUpOnAclException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import java.lang.Long.parseLong

private val logger = KotlinLogging.logger {  }

@Configuration
class WebConfiguration {

    @Bean
    fun webMvcConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            @Autowired lateinit var aclInterceptor : AclInterceptor

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

            override fun addInterceptors(registry: InterceptorRegistry) {
                // Ideally, this would apply to one @RestController, however I couldn't find out if it was possible to specify that
                registry.addInterceptor(aclInterceptor).addPathPatterns("/api/v1/specifications/*/versions/*")
            }
        }
    }
}

@Configuration
class AclInterceptor : HandlerInterceptorAdapter() {
    @Autowired lateinit var jwtAuthenticationProvider: JwtAuthenticationProvider
    @Autowired lateinit var permissionManager: SpecificationPermissionManager

    override fun preHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any): Boolean {
        val header = request.getHeader("Authorization")
        val username = jwtAuthenticationProvider.extractUsername(header)
        val sid = PrincipalSid(username)
        logger.info(SecurityContextHolder.getContext().authentication.toString())

        val pathVariables = AntPathMatcher("/")
                .extractUriTemplateVariables("/api/v1/specifications/{specificationId}/versions/{version}", request.servletPath)
        val asLong = try { parseLong(pathVariables["specificationId"]) } catch (exc: NumberFormatException) { throw GiveUpOnAclException() }

        return when (request.method) {
            "GET" ->                    permissionManager.hasPermission(asLong, sid, BasePermission.READ)
            "POST", "PUT", "DELETE" ->  permissionManager.hasPermission(asLong, sid, BasePermission.WRITE)
            else -> false
        }
    }
}