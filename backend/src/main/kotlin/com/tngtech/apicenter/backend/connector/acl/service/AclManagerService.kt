package com.tngtech.apicenter.backend.connector.acl.service

import com.tngtech.apicenter.backend.domain.entity.Specification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.acls.domain.BasePermission
import org.springframework.stereotype.Service
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.domain.PrincipalSid
import org.springframework.security.acls.model.*
import org.springframework.transaction.TransactionStatus
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.transaction.support.TransactionCallback
import java.io.Serializable
import org.springframework.transaction.support.TransactionCallbackWithoutResult
import org.springframework.transaction.support.TransactionTemplate
import org.springframework.security.acls.model.MutableAcl
import org.springframework.security.acls.model.ObjectIdentity
import java.util.*

interface AclManager {
    fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
    fun <T> removePermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
    fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean
}

@Service
class SpecificationPermissionManager @Autowired constructor(private val aclManagerService : AclManagerService) {
    fun addPermission(id: UUID, version: String, sid: Sid, permission: Permission) =
            aclManagerService.addPermission(Specification::class.java, id.toString() + version, sid, permission)

    fun removePermission(id: UUID, version: String, sid: Sid, permission: Permission) =
            aclManagerService.removePermission(Specification::class.java,id.toString() + version, sid, permission)

    fun hasPermission(id: UUID, version: String, sid: Sid, permission: Permission) =
            aclManagerService.hasPermission(Specification::class.java, id.toString() + version, sid, permission)
}

@Service
class AclManagerService @Autowired constructor(private val aclService: MutableAclService,
                                               private val transactionManager: AbstractPlatformTransactionManager) : AclManager {

    override fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission) {
        TransactionTemplate(transactionManager).execute(object : TransactionCallbackWithoutResult() {
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

    override fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean {
        return TransactionTemplate(transactionManager).execute(object : TransactionCallback<Boolean> {
            override fun doInTransaction(status: TransactionStatus): Boolean {
                val identity = ObjectIdentityImpl(domainClass, id)
                val acl = getOrCreateAcl(identity)
                return try {
                    acl.isGranted(listOf(permission), listOf(sid), false)
                } catch (e: NotFoundException) {
                    false
                }
            }
        }) ?: false
    }

    override fun <T> removePermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission) {
        TransactionTemplate(transactionManager).execute(object : TransactionCallbackWithoutResult() {
            override fun doInTransactionWithoutResult(status: TransactionStatus) {
                val identity = ObjectIdentityImpl(domainClass, id)
                try {
                    val acl = aclService.readAclById(identity) as MutableAcl
                    for (i in 0 until acl.entries.size) {
                        if (acl.entries[i].sid == sid && acl.entries[i].permission == permission) {
                            acl.deleteAce(i)
                        }
                    }
                    aclService.updateAcl(acl)
                } catch (exc: NotFoundException) {
                    // Trying to remove a permission that had never been granted, nothing to do
                    return
                }

            }
        })
    }
}