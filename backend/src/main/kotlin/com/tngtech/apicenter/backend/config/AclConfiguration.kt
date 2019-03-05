package com.tngtech.apicenter.backend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.acls.domain.SpringCacheBasedAclCache
import org.springframework.security.acls.jdbc.BasicLookupStrategy
import org.springframework.security.acls.jdbc.JdbcMutableAclService
import org.springframework.security.acls.AclPermissionEvaluator
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl
import org.springframework.security.acls.domain.ConsoleAuditLogger
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy
import javax.sql.DataSource

@Configuration
class AclConfiguration @Autowired constructor(private val dataSource: DataSource,
                                              private val cacheManager: CacheManager) {

    @Bean
    fun jdbcMutableAclService(): JdbcMutableAclService {
        return JdbcMutableAclService(dataSource, lookupStrategy(), aclCache())
    }

    @Bean
    fun lookupStrategy(): BasicLookupStrategy {
        return BasicLookupStrategy(dataSource, aclCache(), authorizationStrategy(), permissionGrantingStrategy())
    }

    @Bean
    fun aclCache(): SpringCacheBasedAclCache {
        // This fails to build / cache is null when using anything other than the NoOpCache
        val cache = cacheManager.getCache("aclCache")
        return SpringCacheBasedAclCache(cache, permissionGrantingStrategy(), authorizationStrategy())
    }

    @Bean
    fun permissionGrantingStrategy(): DefaultPermissionGrantingStrategy {
        return DefaultPermissionGrantingStrategy(ConsoleAuditLogger())
    }

    @Bean
    fun authorizationStrategy(): AclAuthorizationStrategyImpl {
        return AclAuthorizationStrategyImpl(SimpleGrantedAuthority("ROLE_ADMIN"))
    }

    @Bean
    fun permissionEvaluator(): AclPermissionEvaluator {
        return AclPermissionEvaluator(jdbcMutableAclService())
    }
}