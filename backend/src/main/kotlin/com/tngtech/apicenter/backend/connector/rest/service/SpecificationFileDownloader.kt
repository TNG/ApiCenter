package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.connector.rest.dto.SpecificationFileDto
import com.tngtech.apicenter.backend.domain.exceptions.RemoteFileConnectionRefusedException
import org.springframework.stereotype.Service
import java.net.URL
import java.util.Scanner
import java.net.ConnectException

@Service
class SpecificationFileDownloader {

    fun retrieveFile(location: String): String =
        try {
            Scanner(URL(location).openStream(), "UTF-8").useDelimiter("\\A").next()
        } catch (exception: ConnectException) {
            throw RemoteFileConnectionRefusedException(location)
        }

    fun getLocalOrRemoteFileContent(specificationFileDto: SpecificationFileDto): String {
        return if (specificationFileDto.fileUrl != null && specificationFileDto.fileUrl.isNotBlank()) {
            retrieveFile(specificationFileDto.fileUrl)
        } else {
            specificationFileDto.fileContent ?: ""
        }
    }

}