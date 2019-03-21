package com.tngtech.apicenter.backend.domain.exceptions

class SpecificationNotFoundException(val specificationId: String, val version: String? = "") : RuntimeException()
class SpecificationParseException(val userMessage: String) : RuntimeException()
class VersionAlreadyExistsException(val specificationTitle: String) : RuntimeException()
class UnacceptableUserDefinedApiId(val userDefinedId: String): RuntimeException()
class SpecificationUploadUrlMismatch(val userDefinedId: String, val urlPathId: String): RuntimeException()