package com.tngtech.apicenter.backend.domain.exceptions

class SpecificationNotFoundException(val serviceId: String, val version: String? = "") : RuntimeException()
class SpecificationParseException(val userMessage: String) : RuntimeException()
class SpecificationAlreadyExistsException(val title: String) : RuntimeException()
class InvalidServiceIdException(val userDefinedId: String): RuntimeException()
class MismatchedServiceIdException(val userDefinedId: String, val urlPathId: String): RuntimeException()
class SpecificationDuplicationException: RuntimeException()
class SpecificationConflictException: RuntimeException()
