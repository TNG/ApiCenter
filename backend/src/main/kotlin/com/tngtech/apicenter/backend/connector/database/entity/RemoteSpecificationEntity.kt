package com.tngtech.apicenter.backend.connector.database.entity

import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class RemoteSpecificationEntity(
    @Id @GeneratedValue val id: UUID,
    val title: String,
    val version: String,
    @Column(columnDefinition = "TEXT") val content: String,
    val remoteAddress: String
)