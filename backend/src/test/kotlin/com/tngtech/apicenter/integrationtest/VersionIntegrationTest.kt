package com.tngtech.apicenter.integrationtest

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class VersionIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should retrieve versions`() {
        mockMvc.get("/api/versions").andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].title") { value("versionA") }
            jsonPath("$[0].version") { value("1.0.0") }
            jsonPath("$[0].description") { value("descriptionA") }
            jsonPath("$[0].interfaceId") { value("40d23707-48e8-442a-87f0-a3f15278b745") }
            jsonPath("$[1].title") { value("versionB") }
            jsonPath("$[1].version") { value("2.3.0") }
            jsonPath("$[1].description") { doesNotExist() }
            jsonPath("$[1].interfaceId") { value("40d23707-48e8-442a-87f0-a3f15278b745") }
            jsonPath("$[2].title") { value("versionC") }
            jsonPath("$[2].version") { value("7.0.0") }
            jsonPath("$[2].description") { value("descriptionC") }
            jsonPath("$[2].interfaceId") { value("a134c5b5-0f35-4897-925a-b9c04cd5373b") }
            jsonPath("$[3].title") { value("versionD") }
            jsonPath("$[3].version") { value("2.3.4") }
            jsonPath("$[3].description") { doesNotExist() }
            jsonPath("$[3].interfaceId") { value("c071fa56-8143-477c-8efa-1cbcda42bd04") }
        }
    }

    @Test
    fun `should create and retrieve new version`() {
        mockMvc.post("/api/versions") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "interfaceId": "c071fa56-8143-477c-8efa-1cbcda42bd04",
                    "fileContent": "{\n  \"swagger\": \"2.0.0\",\n  \"info\": {\n    \"version\": \"1.2.3\",\n    \"title\": \"versionE\",\n \"description\": \"descriptionE\"\n  } \n}"
                }
                """.trimIndent()
        }.andExpect {
            status { isCreated() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.title") { value("versionE") }
            jsonPath("$.version") { value("1.2.3") }
            jsonPath("$.description") { value("descriptionE") }
        }

        mockMvc.get("/api/versions").andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[4].title") { value("versionE") }
            jsonPath("$[4].version") { value("1.2.3") }
            jsonPath("$[4].description") { value("descriptionE") }
            jsonPath("$[4].interfaceId") { value("c071fa56-8143-477c-8efa-1cbcda42bd04") }
        }
    }

    @Test
    fun `should fail if no title provided in file`() {
        mockMvc.post("/api/versions") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "interfaceId": "c071fa56-8143-477c-8efa-1cbcda42bd04",
                    "fileContent": "{\n  \"swagger\": \"2.0.0\",\n  \"info\": {\n    \"version\": \"1.2.3\",\n  \"description\": \"descriptionE\"\n  } \n}"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            content { string("Validation error: Title may not be null in OpenAPI file") }
        }
    }

    @Test
    fun `should fail if no version provided in file`() {
        mockMvc.post("/api/versions") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "interfaceId": "c071fa56-8143-477c-8efa-1cbcda42bd04",
                    "fileContent": "{\n  \"swagger\": \"2.0.0\",\n  \"info\": {\n    \"title\": \"versionA\",\n  \"description\": \"descriptionE\"\n  } \n}"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            content { string("Validation error: Version may not be null in OpenAPI file") }
        }
    }

    @Test
    fun `should fail if invalid JSON is sent`() {
        mockMvc.post("/api/versions") {
            contentType = MediaType.APPLICATION_JSON
            content =
                """
                {
                    "interfaceId": "c071fa56-8143-477c-8efa-1cbcda42bd04",
                    "fileContent": "{\n  \"swagger\": \"2.0.0\",\n  \"info\": {\n    \"title\": \"versionA\",\n  \"description\": \"descriptionE\"\n  }"
                }
                """.trimIndent()
        }.andExpect {
            status { isBadRequest() }
            content { string("Validation error: Please provide a valid JSON or YAML file.") }
        }
    }
}
