package com.tngtech.apicenter.backend.connector.rest.dto
import com.fasterxml.jackson.annotation.JsonCreator
import java.util.UUID

data class SpecificationFileDto @JsonCreator constructor(val fileContent: String?, val fileUrl: String? = "", val id: UUID? = null)