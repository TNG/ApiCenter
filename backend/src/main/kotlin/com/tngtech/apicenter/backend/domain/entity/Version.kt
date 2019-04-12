package com.tngtech.apicenter.backend.domain.entity

import com.tngtech.apicenter.backend.connector.rest.dto.VersionMetaData

data class Version(val content: String, val metadata: VersionMetaData)