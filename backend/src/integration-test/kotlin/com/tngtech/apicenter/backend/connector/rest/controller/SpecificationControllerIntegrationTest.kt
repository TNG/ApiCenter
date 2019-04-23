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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class SpecificationControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findOneVersion_shouldReturnVersion() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/v1")
            .with(user("user"))
            .with(csrf()))
            .andExpect(jsonPath("$.metadata.version", equalTo("v1")))
    }

    @Test
    fun findOneVersion_shouldReturnJson() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/v1")
                .header("Accept", "application/json")
                .with(user("user"))
                .with(csrf()))
                .andExpect(jsonPath("$.content", equalTo("{\"info\": {\"title\": \"Spec1\",  \"version\": \"v1\", \"description\": \"Description\"}}")))
    }

    @Test
    fun findOneVersion_shouldReturnYaml() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/v1")
                .header("Accept", "application/yml")
                .with(user("user"))
                .with(csrf()))
                .andExpect(jsonPath("$.content", equalTo("---\ninfo:\n  title: \"Spec1\"\n  version: \"v1\"\n  description: \"Description\"\n")))
    }

    @Test
    fun findOneVersion_shouldGracefullyFail() {
        mockMvc.perform(get("/api/v1/service/b6b06513-d259-4faf-b34b-a216b3daad6a/version/42")
                .with(user("user"))
                .with(csrf()))
                .andExpect(status().isNotFound)
    }


    @Test
    fun deleteVersion_shouldDeleteVersion() {
        mockMvc.perform(delete("/api/v1/service/f67cb0a6-c31b-424b-bfbb-ab0e163955ca/version/v2")
            .with(user("user"))
            .with(csrf()))
            .andExpect(status().isOk)
    }
}