package com.tngtech.apicenter.backend.connector.database.entity

import com.tngtech.apicenter.backend.domain.entity.Role
import org.hibernate.search.annotations.IndexedEmbedded
import javax.persistence.*

@Entity
data class AccessRecordEntity(
        @EmbeddedId @IndexedEmbedded val accessRecordId: AccessRecordId,

        @ManyToOne
        @MapsId("serviceId")
        @JoinColumn(name = "id")
        val serviceEntity: ServiceEntity,

        @ManyToOne
        @MapsId(value = "username")
        @JoinColumn(name = "username")
        val userEntity: UserEntity,

        val role: Role
) {
        override fun hashCode(): Int {
            // Prevent the recursive calls on the ServiceEntity <-> AccessRecordEntity relationship from causing a stack overflow
            return accessRecordId.serviceId.hashCode()
        }

        override fun equals(other: Any?): Boolean {
            if (other == null || other !is AccessRecordEntity) {
                    return false
            }
            return other.accessRecordId.serviceId == accessRecordId.serviceId && other.accessRecordId.username == accessRecordId.username && other.role == role
        }

        override fun toString(): String {
                return accessRecordId.serviceId + " " + accessRecordId.username + " " + role.toString()
        }
}
