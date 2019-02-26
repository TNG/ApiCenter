package com.tngtech.apicenter.backend.domain.entity

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationMetaData

data class Version(val content: String, val metadata: SpecificationMetaData)