package com.tngtech.apicenter.backend.domain.exceptions

import java.util.UUID

class SpecificationNotFoundException(val specificationId: UUID, val version: String? = "") : RuntimeException()
class SpecificationParseException(val userMessage: String) : RuntimeException()
class VersionAlreadyExistsException(val specificationTitle: String) : RuntimeException()