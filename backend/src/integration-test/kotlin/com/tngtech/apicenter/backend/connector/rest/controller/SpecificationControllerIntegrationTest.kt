package com.tngtech.apicenter.backend.connector.rest.controller

import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
internal class SpecificationControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findAllSpecifications_shouldReturnAllSpecifications() {
        mockMvc.perform(get("/api/v1/specifications").with(user("user")))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$[0].title", equalTo("Spec1")))
            .andExpect(jsonPath("$[0].versions[0].metadata.version", equalTo("v1")))
            .andExpect(jsonPath("$[1].title", equalTo("Spec2")))
            .andExpect(jsonPath("$[1].versions[0].metadata.version", equalTo("v1")))
            .andExpect(jsonPath("$[1].versions[1].metadata.version", equalTo("v2")))
            .andExpect(jsonPath("$[2].title", equalTo("Spec3")))
            .andExpect(jsonPath("$[2].versions[0].metadata.version", equalTo("1.0")))
            .andExpect(jsonPath("$[2].versions[1].metadata.version", equalTo("1.1")))
    }

    @Test
    fun findAllSpecifications_requiresAuthentication() {
        mockMvc.perform(get("/api/v1/specifications"))
                .andExpect(status().isForbidden)
    }

    @Test
    fun findOneSpecification_shouldGetOne() {
        mockMvc.perform(get("/api/v1/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user")))
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("title", equalTo("Spec1")))
    }

    @Test
    fun findOneSpecification_shouldGracefullyFail() {
        mockMvc.perform(get("/api/v1/specifications/af0502a2-7410-40e4-90fd-3504f67de1ef").with(user("user")))
                .andExpect(status().isNotFound)
    }

    @Test
    fun uploadSpecification_shouldCreateSpecification() {
        mockMvc.perform(
            post("/api/v1/specifications")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | {
                            |   "fileContent": "{\"info\": {\"title\": \"Spec\",\"version\": \"1.0\"}}"
                            | }
                            """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.metadata.title", equalTo("Spec")))
            .andExpect(jsonPath("$.metadata.version", equalTo("1.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateSpecificationFromYaml() {
        mockMvc.perform(
            post("/api/v1/specifications")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | {
                            |   "fileContent": "openapi: \"3.0.0\"\r\ninfo:\r\n  version: \"1.0\"\r\n  title: YamlSpec"
                            | }
                            """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.metadata.title", equalTo("YamlSpec")))
            .andExpect(jsonPath("$.metadata.version", equalTo("1.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateNewVersion() {
        mockMvc.perform(
            post("/api/v1/specifications")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                        | {
                        |   "fileContent": "{\"info\": {\"title\": \"Spec1\",\"version\": \"v2\",\"x-api-id\": \"unique-identifier\"}}"
                        | }
                    """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.metadata.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.metadata.version", equalTo("v2")))

        mockMvc.perform(
            get("/api/v1/specifications/unique-identifier")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(jsonPath("$.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.versions[0].metadata.version", equalTo("v2")))
            .andExpect(jsonPath("$.versions[0].metadata.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.versions[1].metadata.version", equalTo("v1")))
            .andExpect(jsonPath("$.versions[1].metadata.title", equalTo("Spec4")))
    }

    @Test
    fun updateSpecification_shouldUpdateSpecification() {
        mockMvc.perform(
            put("/api/v1/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                            | {
                            |   "id": "b6b06513-d259-4faf-b34b-a216b3daad6a",
                            |   "fileContent": "{\"info\": {\"title\": \"NewSpec\",\"version\": \"vX\"}}"
                            | }
                            """.trimMargin()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.metadata.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.metadata.version", equalTo("vX")))

        mockMvc.perform(
            get("/api/v1/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user"))
        )
            .andExpect(jsonPath("$.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.versions[0].metadata.version", equalTo("vX")))
    }

    @Test
    fun updateSpecification_shouldRejectIdMismatchInXApiId() {
        mockMvc.perform(
                put("/api/v1/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a")
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
                put("/api/v1/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a")
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
    fun deleteSpecification_shouldDeleteSpecification() {
        mockMvc.perform(
            delete("/api/v1/specifications/af0502a2-7410-40e4-90fd-3504f67de1ee")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(status().isOk)
    }
}