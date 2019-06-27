package com.tngtech.apicenter.backend.connector.rest.controller

import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class ServiceControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findAllServices_shouldReturnAllServices() {
        mockMvc.perform(get("/api/v1/service").with(user("user")))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$[0].title", equalTo("Spec1")))
            .andExpect(jsonPath("$[0].specifications[0].metadata.version", equalTo("1.0.0")))
            .andExpect(jsonPath("$[1].title", equalTo("Spec2")))
            .andExpect(jsonPath("$[1].specifications[0].metadata.version", equalTo("1.0.0")))
            .andExpect(jsonPath("$[1].specifications[1].metadata.version", equalTo("2.0.0")))
            .andExpect(jsonPath("$[2].title", equalTo("Spec3")))
            .andExpect(jsonPath("$[2].specifications[0].metadata.version", equalTo("1.0.0")))
            .andExpect(jsonPath("$[2].specifications[1].metadata.version", equalTo("1.1.0")))
    }

    @Test
    fun findAllServices_requiresAuthentication() {
        mockMvc.perform(get("/api/v1/service"))
                .andExpect(status().isForbidden)
    }

    @Test
    fun findOneService_shouldGetOne() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user")))
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("title", equalTo("Spec1")))
    }

    @Test
    fun findOneService_shouldGracefullyFail() {
        mockMvc.perform(get("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ef").with(user("user")))
                .andExpect(status().isNotFound)
    }

    @Test
    fun uploadSpecification_shouldCreateService() {
        mockMvc.perform(
            post("/api/v1/service")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | [{
                            |   "fileContent": "{\"info\": {\"title\": \"Spec\",\"version\": \"1.0.0\"}}"
                            | }]
                            """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$[0].metadata.title", equalTo("Spec")))
            .andExpect(jsonPath("$[0].metadata.version", equalTo("1.0.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateSpecificationWithMetadata() {
        mockMvc.perform(
                post("/api/v1/service")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content("""
                           [{"fileContent":"t","metadata":{"title":"My title","version":"3.0.0","description":"","language":"GRAPHQL","releaseType":"RELEASE","endpointUrl":""}}]
                        """.trimIndent()
                        )
        )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$[0].metadata.title", equalTo("My title")))
                .andExpect(jsonPath("$[0].metadata.version", equalTo("3.0.0")))
    }

    @Test
    fun uploadSpecification_shouldDetectVersionClash_identicalContent() {
        mockMvc.perform(
                post("/api/v1/service")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(
                                """
                            | [{
                            |   "id": "b6b06513-d259-4faf-b34b-a216b3daad6a",
                            |   "fileContent": "{\"info\": {\"title\": \"Spec1\",  \"version\": \"1.0.0\", \"description\": \"Description\"}}"
                            | }]
                            """.trimMargin()
                        )
        )
                .andExpect(status().isAccepted)
    }

    @Test
    fun uploadSpecification_shouldDetectVersionClash_differentContent() {
        mockMvc.perform(
                post("/api/v1/service")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(
                                """
                            | [{
                            |   "id": "b6b06513-d259-4faf-b34b-a216b3daad6a",
                            |   "fileContent": "{\"info\": {\"title\": \"Spec1\",  \"version\": \"1.0.0\", \"description\": \"I'm different\"}}"
                            | }]
                            """.trimMargin()
                        )
        )
                .andExpect(status().isConflict)
    }

    @Test
    fun uploadSpecification_shouldDetectVersionClash_overwriteSnapshot() {
        mockMvc.perform(
                post("/api/v1/service")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(
                                """
                            | {
                            |   "id": "unique-identifier-2",
                            |   "fileContent": "{\"info\": {\"title\": \"Spec1\",  \"version\": \"1.0.0-SNAPSHOT\", \"description\": \"I'm different\"}}"
                            | }
                            """.trimMargin()
                        )
        )
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.metadata.description", equalTo("I'm different")))
    }

    @Test
    fun uploadSpecification_shouldCreateSpecificationFromYaml() {
        mockMvc.perform(
            post("/api/v1/service")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | [{
                            |   "fileContent": "openapi: \"3.0.0\"\r\ninfo:\r\n  version: \"1.0.0\"\r\n  title: YamlSpec"
                            | }]
                            """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$[0].metadata.title", equalTo("YamlSpec")))
            .andExpect(jsonPath("$[0].metadata.version", equalTo("1.0.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateNewSpecification() {
        mockMvc.perform(
            post("/api/v1/service")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                        | [{
                        |   "fileContent": "{\"info\": {\"title\": \"Spec1\",\"version\": \"2.0.0\",\"x-api-id\": \"unique-identifier\"}}"
                        | }]
                    """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$[0].metadata.title", equalTo("Spec1")))
            .andExpect(jsonPath("$[0].metadata.version", equalTo("2.0.0")))

        mockMvc.perform(
            get("/api/v1/service/unique-identifier")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(jsonPath("$.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.specifications[0].metadata.version", equalTo("2.0.0")))
            .andExpect(jsonPath("$.specifications[0].metadata.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.specifications[1].metadata.version", equalTo("1.0.0")))
            .andExpect(jsonPath("$.specifications[1].metadata.title", equalTo("Spec4")))
    }

    @Test
    fun updateSpecification_shouldUpdateSpecification() {
        mockMvc.perform(
            put("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | {
                            |   "id": "b6b06513-d259-4faf-b34b-a216b3daad6a",
                            |   "fileContent": "{\"info\": {\"title\": \"NewSpec\",\"version\": \"1.0.1\"}}"
                            | }
                            """.trimMargin()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.metadata.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.metadata.version", equalTo("1.0.1")))

        mockMvc.perform(
            get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user"))
        )
            .andExpect(jsonPath("$.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.specifications[0].metadata.version", equalTo("1.0.1")))
    }

    @Test
    fun updateSpecification_shouldRejectIdMismatchInXApiId() {
        mockMvc.perform(
                put("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(
                                """
                            | {
                            |   "fileContent": "{\"info\": {\"title\": \"NewSpec\",\"version\": \"vX\", \"x-api-id\": \"does-not-match\"}}"
                            | }
                            """.trimMargin()
                        )
        )
                .andExpect(status().isBadRequest)
    }

    @Test
    fun updateSpecification_shouldRejectIdMismatchInDto() {
        mockMvc.perform(
                put("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a")
                        .with(user("user"))
                        .with(csrf())
                        .contentType("application/json")
                        .content(
                                """
                            | {
                            |   "id": "mismatch",
                            |   "fileContent": "{\"info\": {\"title\": \"NewSpec\",\"version\": \"vX\"}}"
                            | }
                            """.trimMargin()
                        )
        )
                .andExpect(status().isBadRequest)
    }

    @Test
    fun deleteService_shouldDeleteService() {
        mockMvc.perform(
            delete("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ee")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(status().isOk)
    }
}