package com.tngtech.apicenter.backend.connector.rest.dto

import com.fasterxml.jackson.annotation.JsonCreator
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.UUID

@ApiModel(description = "Represents an OpenAPI-File")
data class SpecificationFileDto @JsonCreator constructor(
    @ApiModelProperty("The content of a file to be uploaded - either this or fileUrl has to be set.") val fileContent: String?,
    @ApiModelProperty("The url of a remote file to be loaded - either this or fileContent has to be set.") val fileUrl: String? = "",
    @ApiModelProperty("The uuid of an already existing specification (optional)") val id: UUID? = null
)