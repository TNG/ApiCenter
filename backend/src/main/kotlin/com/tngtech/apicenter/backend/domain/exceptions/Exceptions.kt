package com.tngtech.apicenter.backend.domain.exceptions

class SpecificationNotFoundException(val serviceId: String, val version: String? = "") : RuntimeException()
class SpecificationParseException(val userMessage: String) : RuntimeException()
class SpecificationAlreadyExistsException(val title: String) : RuntimeException()
class InvalidServiceIdException(val userDefinedId: String): RuntimeException()
class MismatchedServiceIdException(val userDefinedId: String, val urlPathId: String): RuntimeException()
class SpecificationDuplicationException: RuntimeException()
class SpecificationConflictException: RuntimeException()
class RemoteFileConnectionRefusedException(val location: String): RuntimeException()
class BadUrlException(val location: String): RuntimeException()
class PermissionDeniedException(val serviceId: String): RuntimeException()
