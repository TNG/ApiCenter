package com.tngtech.apicenter.backend.domain.entity

class ResultPage<T>(
        val content: List<T>,
        val last: Boolean
) {
    fun <U> map(callback: (element: T) -> U): ResultPage<U> = ResultPage(this.content.map(callback), this.last)
}
