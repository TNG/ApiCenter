package com.tngtech.apicenter.backend.domain.entity

import com.tngtech.apicenter.backend.domain.exceptions.InvalidSpecificationIdException

class ServiceId {
    val id: String

    constructor(id: String) {
        if (containsInvalidCharacters(id)) throw InvalidSpecificationIdException(id)
        this.id = id
    }

    private fun containsInvalidCharacters(id: String): Boolean {
        return !id.matches("^[\\w_\\-]+$".toRegex())
    }

    override fun equals(other: Any?): Boolean {
        return if (other == null || other::class != this::class) {
            false
        } else {
            this.id == (other as ServiceId).id
        }
    }

    override fun hashCode(): Int {
        return this.id.hashCode()
    }
}
