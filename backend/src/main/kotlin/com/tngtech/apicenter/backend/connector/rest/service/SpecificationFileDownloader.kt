package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import org.springframework.stereotype.Service
import java.net.URL
import java.util.Scanner

@Service
class SpecificationFileDownloader {

    fun retrieveFile(location: String): String {
        return Scanner(URL(location).openStream(), "UTF-8").useDelimiter("\\A").next()
    }

    fun getLocalOrRemoteFileContent(specificationFileDto: SpecificationFileDto): String {
        return if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl.isNotBlank()) {
            retrieveFile(specificationFileDto.fileUrl)
        } else {
            specificationFileDto.fileContent ?: ""
        }
    }

}