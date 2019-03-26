package com.tngtech.apicenter.backend.connector.rest.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class GraphQLController {
    @RequestMapping("/api/v1/graphql", method = [RequestMethod.GET, RequestMethod.POST])
    @ResponseStatus(
            code = HttpStatus.NOT_FOUND,
            reason = "No graphql API endpoint was specified for this specification. Add an API URL to use GraphiQL"
    )
    fun noGraphQLEndpointSpecifiedHandler() {}
}