package com.tngtech.apicenter.backend.connector.acl.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.*
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import java.io.Serializable
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate

interface AclManager {
    fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
//    fun <T> removePermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
    fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean
}

@Service
class AclManagerService @Autowired constructor(private val aclService: MutableAclService,
                                               private val transactionManager: AbstractPlatformTransactionManager) : AclManager {

    override fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission) {
        val template = TransactionTemplate(transactionManager)

        template.execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                val identity = ObjectIdentityImpl(domainClass, id)
                val acl = getOrCreateAcl(identity)
                acl.insertAce(acl.entries.size, permission, sid, true)
                aclService.updateAcl(acl)
            }
        })
    }

    fun getOrCreateAcl(identity: ObjectIdentity): MutableAcl {
        return try {
            aclService.readAclById(identity) as MutableAcl
        } catch (exc: NotFoundException) {
            aclService.createAcl(identity)
        }
    }

    @Transactional
    override fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean {
        val identity = ObjectIdentityImpl(domainClass, id)
        val acl = getOrCreateAcl(identity)
        return acl.isGranted(listOf(permission), listOf(sid), false)
    }
}