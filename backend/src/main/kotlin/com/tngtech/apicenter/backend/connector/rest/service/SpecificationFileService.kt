package com.tngtech.apicenter.backend.connector.rest.service

import com.tngtech.apicenter.backend.connector.rest.dto.VersionFileDto
import org.springframework.stereotype.Service
import java.net.URL
import java.util.Scanner

@Service
class SpecificationFileService {

    fun retrieveFile(location: String): String {
        return Scanner(URL(location).openStream(), "UTF-8").useDelimiter("\\A").next()
    }

    fun getLocalOrRemoteFileContent(versionFileDto: VersionFileDto): String {
        return if (versionFileDto.fileUrl != null && versionFileDto.fileUrl.isNotBlank()) {
            retrieveFile(versionFileDto.fileUrl)
        } else {
            versionFileDto.fileContent ?: ""
        }
    }

}