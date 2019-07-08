package com.tngtech.apicenter.backend.domain.entity

class ResultPage<T> constructor(
        val content: List<T>,
        val isLast: Boolean
) {
    fun <U> map(callback: (element: T) -> U): ResultPage<U> =
            ResultPage(this.content.map(callback), this.isLast)
}
