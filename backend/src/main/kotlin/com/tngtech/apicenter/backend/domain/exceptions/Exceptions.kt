package com.tngtech.apicenter.backend.domain.exceptions

class SpecificationNotFoundException(val specificationId: String, val version: String? = "") : RuntimeException()
class SpecificationParseException(val userMessage: String) : RuntimeException()
class VersionAlreadyExistsException(val specificationTitle: String) : RuntimeException()
class InvalidSpecificationIdException(val userDefinedId: String): RuntimeException()
class MismatchedSpecificationIdException(val userDefinedId: String, val urlPathId: String): RuntimeException()
class IdNotCastableToLongException: RuntimeException()
class AclPermissionDeniedException(val specificationId: String): RuntimeException()