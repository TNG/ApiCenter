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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
internal class SpecificationControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findAllSpecifications_shouldReturnAllSpecifications() {
        mockMvc.perform(get("/api/1.0/specifications").with(user("user")))
            .andExpect(status().isOk)
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$[0].title", equalTo("Spec1")))
            .andExpect(jsonPath("$[0].versions[0].version", equalTo("v1")))
            .andExpect(jsonPath("$[1].title", equalTo("Spec2")))
            .andExpect(jsonPath("$[1].versions[0].version", equalTo("v1")))
            .andExpect(jsonPath("$[1].versions[1].version", equalTo("v2")))
            .andExpect(jsonPath("$[2].title", equalTo("Spec3")))
            .andExpect(jsonPath("$[2].versions[0].version", equalTo("1.0")))
            .andExpect(jsonPath("$[2].versions[1].version", equalTo("1.1")))
    }

    @Test
    fun findAllSpecifications_requiresAuthentication() {
        mockMvc.perform(get("/api/1.0/specifications"))
                .andExpect(status().`is`(403))
    }

    @Test
    fun findOneSpecification_shouldGetOne() {
        mockMvc.perform(get("/api/1.0/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user")))
                .andExpect(status().isOk)
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("title", equalTo("Spec1")))
    }

    @Test
    fun findOneSpecification_shouldGracefullyFail() {
        mockMvc.perform(get("/api/1.0/specifications/af0502a2-7410-40e4-90fd-3504f67de1ef").with(user("user")))
                .andExpect(status().`is`(404))
    }

    @Test
    fun uploadSpecification_shouldCreateSpecification() {
        mockMvc.perform(
            post("/api/1.0/specifications")
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
            .andExpect(jsonPath("$.title", equalTo("Spec")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("1.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateSpecificationFromYaml() {
        mockMvc.perform(
            post("/api/1.0/specifications")
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
            .andExpect(jsonPath("$.title", equalTo("YamlSpec")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("1.0")))
    }

    @Test
    fun uploadSpecification_shouldCreateNewVersion() {
        mockMvc.perform(
            post("/api/1.0/specifications")
                .with(user("user"))
                .with(csrf())
                .contentType("application/json")
                .content(
                    """
                        | {
                        |   "fileContent": "{\"info\": {\"title\": \"Spec1\",\"version\": \"v2\"}}"
                        | }
                    """.trimMargin()
                )
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("v2")))

        mockMvc.perform(
            get("/api/1.0/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(jsonPath("$.title", equalTo("Spec1")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("v2")))
            .andExpect(jsonPath("$.versions[1].version", equalTo("v1")))
    }

    @Test
    fun updateSpecification_shouldUpdateSpecification() {
        mockMvc.perform(
            put("/api/1.0/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a")
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
            .andExpect(jsonPath("$.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("vX")))

        mockMvc.perform(
            get("/api/1.0/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a").with(user("user"))
        )
            .andExpect(jsonPath("$.title", equalTo("NewSpec")))
            .andExpect(jsonPath("$.versions[0].version", equalTo("vX")))
    }

    @Test
    fun deleteSpecification_shouldDeleteSpecification() {
        mockMvc.perform(
            delete("/api/1.0/specifications/af0502a2-7410-40e4-90fd-3504f67de1ee")
                .with(user("user"))
                .with(csrf())
        )
            .andExpect(status().isOk)
    }
}