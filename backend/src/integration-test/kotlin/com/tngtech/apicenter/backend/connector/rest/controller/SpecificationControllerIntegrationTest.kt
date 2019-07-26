package com.tngtech.apicenter.backend.connector.rest.controller

import com.tngtech.apicenter.backend.connector.rest.security.JwtAuthenticationToken
import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SpecificationControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    private val userRequestPostProcessor = authentication(JwtAuthenticationToken("user", "irrelevant"))
    private val otherRequestPostProcessor = authentication(JwtAuthenticationToken("other", "irrelevant"))

    @Test
    fun findOneSpecification_shouldReturnSpecification() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/1.0.0")
            .with(userRequestPostProcessor)
            .with(csrf()))
            .andExpect(jsonPath("$.metadata.version", equalTo("1.0.0")))
    }

    @Test
    fun findOneSpecification_requiresAuthentication() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/1.0.0")
                .with(csrf()))
                .andExpect(status().isNotFound)
    }

    @Test
    fun findOneSpecification_shouldReturnJson() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/1.0.0")
                .header("Accept", "application/json")
                .with(userRequestPostProcessor)
                .with(csrf()))
                .andExpect(jsonPath("$.content", equalTo("{\"info\": {\"title\": \"Spec1\",  \"version\": \"1.0.0\", \"description\": \"Description\"}}")))
    }

    @Test
    fun findOneSpecification_shouldReturnYaml() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/1.0.0")
                .header("Accept", "application/yml")
                .with(userRequestPostProcessor)
                .with(csrf()))
                .andExpect(jsonPath("$.content", equalTo("---\ninfo:\n  title: \"Spec1\"\n  version: \"1.0.0\"\n  description: \"Description\"\n")))
    }

    @Test
    fun findOneSpecification_shouldGracefullyFail() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/42")
                .with(userRequestPostProcessor)
                .with(csrf()))
                .andExpect(status().isNotFound)
    }


    @Test
    fun deleteSpecification_shouldDeleteSpecification() {
        mockMvc.perform(delete("/api/v1/service/f67cb0a6-c31b-424b-bfbb-ab0e163955ca/version/2.0.0")
            .with(userRequestPostProcessor)
            .with(csrf()))
            .andExpect(status().isOk)
    }

    @Test
    fun addViewPermissionSucceeds() {
        mockMvc.perform(
                get("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ee/version/1.0.0")
                        .with(userRequestPostProcessor)
                        .with(csrf())
        )
                .andExpect(status().isOk)

        mockMvc.perform(
                get("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ee/version/1.0.0")
                        .with(otherRequestPostProcessor)
                        .with(csrf())
        )
                .andExpect(status().isNotFound)

        mockMvc.perform(
                put("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ee/permissions/other?role=VIEWER")
                        .with(userRequestPostProcessor)
                        .with(csrf())
        )
                .andExpect(status().isOk)

        mockMvc.perform(
                get("/api/v1/service/af0502a2-7410-40e4-90fd-3504f67de1ee/version/1.0.0")
                        .with(otherRequestPostProcessor)
                        .with(csrf())
        )
                .andExpect(status().isOk)
    }
}