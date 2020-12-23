package com.tngtech.apicenter.integrationtest

import com.fasterxml.jackson.databind.ObjectMapper
import com.tngtech.apicenter.dto.InterfaceDto
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
class InterfaceIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `should retrieve interfaces`() {
        mockMvc.get("/api/interfaces").andExpect {
            status { isOk }
            jsonPath("$[0].name") { value("interfaceA") }
            jsonPath("$[0].description") { value("interfaceDescriptionA") }
            jsonPath("$[0].type") { value("REST") }
            jsonPath("$[1].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
            jsonPath("$[1].name") { value("interfaceB") }
            jsonPath("$[1].description") { doesNotExist() }
            jsonPath("$[1].type") { value("REST") }
            jsonPath("$[1].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
            jsonPath("$[2].name") { value("interfaceC") }
            jsonPath("$[2].description") { value("interfaceDescriptionC") }
            jsonPath("$[2].type") { value("GRAPHQL") }
            jsonPath("$[2].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
            jsonPath("$[3].name") { value("interfaceD") }
            jsonPath("$[3].description") { doesNotExist() }
            jsonPath("$[3].type") { value("GRAPHQL") }
            jsonPath("$[3].applicationId") { value("46a9df3f-c37a-499e-a407-14c5158d2ac5") }
            jsonPath("$[4].name") { value("interfaceE") }
            jsonPath("$[4].description") { value("interfaceDescriptionE") }
            jsonPath("$[4].type") { value("REST") }
            jsonPath("$[4].applicationId") { value("46a9df3f-c37a-499e-a407-14c5158d2ac5") }
        }
    }

    @Test
    fun `should retrieve interfaces for a specific application`() {
        mockMvc.get("/api/applications/168a1a96-337c-41e6-b3a6-1e8c43cdf863/interfaces").andExpect {
            status { isOk }
            jsonPath("$[0].name") { value("interfaceA") }
            jsonPath("$[0].description") { value("interfaceDescriptionA") }
            jsonPath("$[0].type") { value("REST") }
            jsonPath("$[1].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
            jsonPath("$[1].name") { value("interfaceB") }
            jsonPath("$[1].description") { doesNotExist() }
            jsonPath("$[1].type") { value("REST") }
            jsonPath("$[1].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
            jsonPath("$[2].name") { value("interfaceC") }
            jsonPath("$[2].description") { value("interfaceDescriptionC") }
            jsonPath("$[2].type") { value("GRAPHQL") }
            jsonPath("$[2].applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
        }

        mockMvc.get("/api/applications/ad374016-d15d-47af-8d82-96fceb6d51fa/interfaces").andExpect {
            status { isOk }
            content { string("[]") }
        }
    }

    @Test
    fun `should create and retrieve interface`() {
        val result = mockMvc.post("/api/interfaces") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "name": "interfaceToCreate",
                    "description": "thisIsTheInterfaceToBeCreated",
                    "type": "GRAPHQL",
                    "applicationId": "168a1a96-337c-41e6-b3a6-1e8c43cdf863"
                }
                """.trimIndent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("interfaceToCreate") }
            jsonPath("$.description") { value("thisIsTheInterfaceToBeCreated") }
            jsonPath("$.type") { value("GRAPHQL") }
            jsonPath("$.applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
        }.andReturn()

        val interfaceResult = objectMapper.readValue(result.response.contentAsString, InterfaceDto::class.java)

        mockMvc.get("/api/interfaces/${interfaceResult.id}").andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("interfaceToCreate") }
            jsonPath("$.description") { value("thisIsTheInterfaceToBeCreated") }
            jsonPath("$.type") { value("GRAPHQL") }
            jsonPath("$.applicationId") { value("168a1a96-337c-41e6-b3a6-1e8c43cdf863") }
        }
    }

    @Test
    fun `should return error when no name is provided`() {
        mockMvc.post("/api/interfaces") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "description": "thisIsTheInterfaceToBeCreated",
                    "type": "GRAPHQL",
                    "applicationId": "168a1a96-337c-41e6-b3a6-1e8c43cdf863"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `should return error when a non-existing type is provided`() {
        mockMvc.post("/api/interfaces") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "name": "interfaceToCreate",
                    "description": "thisIsTheInterfaceToBeCreated",
                    "type": "NON-EXISTING-TYPE",
                    "applicationId": "168a1a96-337c-41e6-b3a6-1e8c43cdf863"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `should update interface`() {
        mockMvc.put("/api/interfaces/1660d4df-25e3-4aed-8b14-83842c9f3f1f") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "name": "interfaceX",
                    "description": "interfaceDescriptionE",
                    "type": "REST",
                    "applicationId": "46a9df3f-c37a-499e-a407-14c5158d2ac5"
                }
                """.trimIndent()
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("interfaceX") }
            jsonPath("$.description") { value("interfaceDescriptionE") }
            jsonPath("$.type") { value("REST") }
            jsonPath("$.applicationId") { value("46a9df3f-c37a-499e-a407-14c5158d2ac5") }
        }
    }

    @Test
    fun `should throw error when no name is provided for update`() {
        mockMvc.put("/api/interfaces/1660d4df-25e3-4aed-8b14-83842c9f3f1f") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "description": "interfaceDescriptionE",
                    "type": "REST",
                    "applicationId": "46a9df3f-c37a-499e-a407-14c5158d2ac5"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest }
        }
    }

    @Test
    fun `should delete interface`() {
        mockMvc.delete("/api/interfaces/1660d4df-25e3-4aed-8b14-83842c9f3f1f").andExpect {
            status { isNoContent }
        }
    }
}
