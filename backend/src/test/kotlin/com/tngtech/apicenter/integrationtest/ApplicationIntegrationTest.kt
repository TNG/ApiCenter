package com.tngtech.apicenter.integrationtest

import com.fasterxml.jackson.databind.ObjectMapper
import com.tngtech.apicenter.dto.ApplicationDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should create and retrieve application`() {
        val result = mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationToCreate",
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationToCreate") }
            jsonPath("$.description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$.contact") { value("myContact") }
        }.andReturn()

        val applicationResult = objectMapper.readValue(result.response.contentAsString, ApplicationDto::class.java)

        mockMvc.get("/api/applications").andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].name") { value("applicationToCreate") }
            jsonPath("$[0].description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$[0].contact") { value("myContact") }
        }

        mockMvc.get("/api/applications/${applicationResult.id}").andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationToCreate") }
            jsonPath("$.description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$.contact") { value("myContact") }
        }
    }

    @Test
    fun `should create and update application`() {
        val result = mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationToCreate",
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationToCreate") }
            jsonPath("$.description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$.contact") { value("myContact") }
        }.andReturn()

        val applicationResult = objectMapper.readValue(result.response.contentAsString, ApplicationDto::class.java)

        mockMvc.put("/api/applications/${applicationResult.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "newApplicationName",
                    "description": "newApplicationDescription",
                    "contact": "newApplicationContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("newApplicationName") }
            jsonPath("$.description") { value("newApplicationDescription") }
            jsonPath("$.contact") { value("newApplicationContact") }
        }

        mockMvc.get("/api/applications/${applicationResult.id}").andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("newApplicationName") }
            jsonPath("$.description") { value("newApplicationDescription") }
            jsonPath("$.contact") { value("newApplicationContact") }
        }
    }

    @Test
    fun `should create and delete application`() {
        val result = mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationToCreate",
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationToCreate") }
            jsonPath("$.description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$.contact") { value("myContact") }
        }.andReturn()

        val applicationResult = objectMapper.readValue(result.response.contentAsString, ApplicationDto::class.java)

        mockMvc.delete("/api/applications/${applicationResult.id}").andExpect {
            status { isNoContent }
        }
    }

    @Test
    fun `should throw error when no name is submitted for application creation`() {
        mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `should throw error when no name is submitted for application update`() {
        val result = mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationName",
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andReturn()

        val applicationResult = objectMapper.readValue(result.response.contentAsString, ApplicationDto::class.java)

        mockMvc.put("/api/applications/${applicationResult.id}") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `should throw error when non existing application is retrieved or deleted`() {
        mockMvc.get("/api/applications/777b882d-df3d-4ffe-b9cc-ffdb591896ec").andExpect {
            status { isNotFound }
        }

        mockMvc.delete("/api/applications/777b882d-df3d-4ffe-b9cc-ffdb591896ec").andExpect {
            status { isNotFound }
        }
    }
}
