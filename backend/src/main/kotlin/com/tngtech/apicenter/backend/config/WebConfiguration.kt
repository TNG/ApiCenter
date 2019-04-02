package com.tngtech.apicenter.backend.config

import com.tngtech.apicenter.backend.connector.acl.service.SpecificationPermissionManager
import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.acls.domain.BasePermission
import org.springframework.util.AntPathMatcher
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.lang.Long.parseLong

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
                registry.addViewController("/edit-permissions/**").setViewName("forward:/index.html")
                registry.addViewController("/search").setViewName("forward:/index.html")
                registry.addViewController("/search/**").setViewName("forward:/index.html")
                registry.addViewController("/login").setViewName("forward:/index.html")
            }

            override fun addInterceptors(registry: InterceptorRegistry) {
                registry.addInterceptor(aclInterceptor).addPathPatterns("/api/v1/specifications/**")
            }
        }
    }
}

@Configuration
class AclInterceptor constructor(private val jwtAuthenticationProvider: JwtAuthenticationProvider): HandlerInterceptorAdapter() {
    @Autowired lateinit var permissionManager: SpecificationPermissionManager

    override fun preHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any): Boolean {
        val sid = jwtAuthenticationProvider.getCurrentPrincipal()

        val pathVariables = try {
            AntPathMatcher("/")
                    .extractUriTemplateVariables("/api/v1/specifications/{specificationId}/**", request.servletPath)
        } catch (exception: IllegalStateException) {
            return true
        }

        val asLong = try {
            parseLong(pathVariables["specificationId"])
        } catch (exc: NumberFormatException) {
            return true
        }

        return when (request.method) {
            "GET" ->                    permissionManager.hasPermission(asLong, sid, BasePermission.READ)
            "POST", "PUT", "DELETE" ->  permissionManager.hasPermission(asLong, sid, BasePermission.WRITE)
            else -> false
        }
    }
}