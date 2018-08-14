package com.tngtech.apicenter.backend.connector.rest.service

import org.springframework.stereotype.Service
import java.net.URL
import java.util.Scanner

@Service
class SpecificationFileService {

    fun retrieveFile(location: String): String {
        return Scanner(URL(location).openStream(), "UTF-8").useDelimiter("\\A").next()
    }
}