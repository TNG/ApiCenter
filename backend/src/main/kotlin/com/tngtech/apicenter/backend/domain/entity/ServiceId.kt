package com.tngtech.apicenter.backend.domain.entity

import com.tngtech.apicenter.backend.domain.exceptions.InvalidServiceIdException

data class ServiceId(val id: String) {

    init {
        if (containsInvalidCharacters(id)) {
            throw InvalidServiceIdException(id)
        }
    }

    private fun containsInvalidCharacters(id: String): Boolean {
        return !id.matches("^[\\w_\\-]+$".toRegex())
    }
}
