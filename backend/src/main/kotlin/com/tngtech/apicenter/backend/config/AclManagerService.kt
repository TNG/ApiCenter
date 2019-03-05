package com.tngtech.apicenter.backend.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.security.acls.domain.ObjectIdentityImpl
import org.springframework.security.acls.model.*
import java.io.Serializable

interface AclManager {
    fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
//    fun <T> removePermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission)
    fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean
}

@Service
class AclManagerService @Autowired constructor(private val aclService: MutableAclService) : AclManager {

    override fun <T> addPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission) {
        val identity = ObjectIdentityImpl(domainClass, id)
        val acl = getOrCreateAcl(identity)
        acl.updateAce(acl.entries.size, permission)
        aclService.updateAcl(acl)
    }

    private fun getOrCreateAcl(identity: ObjectIdentity): MutableAcl {
        return try {
            aclService.readAclById(identity) as MutableAcl
        } catch (exc: NotFoundException) {
            aclService.createAcl(identity)
        }
    }

    override fun <T> hasPermission(domainClass: Class<T>, id: Serializable, sid: Sid, permission: Permission): Boolean {
        val identity = ObjectIdentityImpl(domainClass, id)
        val acl = aclService.readAclById(identity)
        return acl.isGranted(listOf(permission), listOf(sid), false)
    }
}