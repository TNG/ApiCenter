package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.dto.VersionDto
import com.tngtech.apicenter.backend.connector.rest.mapper.VersionDtoMapper
import com.tngtech.apicenter.backend.domain.handler.VersionHandler
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

@RestController
class VersionController constructor(private val versionHandler: VersionHandler, private val versionDtoMapper: VersionDtoMapper) {
    // @GetMapping("/specifications/{specificationId}/versions/{version}")
    // fun findVersion(@PathVariable specificationId: UUID, @PathVariable version: String): VersionDto = versionDtoMapper.fromDomain(versionHandler.findOne(specificationId, version))


    @GetMapping("/specifications/{specificationId}/versions/{version}")
    fun downloadVersion(@PathVariable specificationId: UUID, @PathVariable version: String, response: HttpServletResponse) {
        logger.info("Download version called")
        // For now, this function returns a static response, just to test the routing
        // The following lines may come in handy later
        // val foundVersion = versionHandler.findOne(specificationId, version)
        // response.setContentType("text/yaml")
        // response.setHeader("Content-Disposition", String.format("attachment, filename=\"%s\"", specificationId))
        // This disposition causes a dialog box to appear, as opposed to "inline" which is previewed in browser (eg. images)
        //response.getWriter().write(foundVersion.content)

        // Write the API content to the reponse
        response.setContentType("text/plain")
        response.getWriter().write("my response content")
        response.getWriter().flush()

        response.flushBuffer()
    }
    

    @DeleteMapping("/specifications/{specificationId}/versions/{version}")
    fun deleteVersion(@PathVariable specificationId: UUID, @PathVariable version: String) {
        logger.info("Delete version called")
        versionHandler.delete(specificationId, version)
    }
}