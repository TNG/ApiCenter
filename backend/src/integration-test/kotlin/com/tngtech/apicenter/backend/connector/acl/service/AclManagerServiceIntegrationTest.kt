package com.tngtech.apicenter.backend.connector.acl.service

import com.tngtech.apicenter.backend.config.AclConfiguration
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.security.acls.domain.BasePermission
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.MutableAclService
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.support.AbstractPlatformTransactionManager

data class DomainObject(val id: Int)

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [AclConfiguration::class])
class AclManagerServiceIntegrationTest {
    @Before
    fun setUp() {
        aclManagerService = AclManagerService(aclService, transactionManager)
    }

    @Autowired
    lateinit var aclService : MutableAclService

    @Autowired
    lateinit var transactionManager: AbstractPlatformTransactionManager

    private lateinit var aclManagerService : AclManagerService

    @Test
    fun addPermission_shouldGrantAccess() {
        val resource = DomainObject(0)
        val sid = PrincipalSid("user")
        assertThat(aclManagerService.hasPermission(resource.javaClass, resource.id, sid, BasePermission.READ)).isFalse()
        aclManagerService.addPermission(resource.javaClass, resource.id, sid, BasePermission.READ)
        assertThat(aclManagerService.hasPermission(resource.javaClass, resource.id, sid, BasePermission.READ)).isTrue()
    }
}