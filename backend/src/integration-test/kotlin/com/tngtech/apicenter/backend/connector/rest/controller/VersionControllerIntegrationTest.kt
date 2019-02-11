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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class VersionControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findOneVersion_shouldReturnVersion() {
        mockMvc.perform(get("/api/1.0/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/v1")
            .with(user("user"))
            .with(csrf()))
            .andExpect(jsonPath("$.version", equalTo("v1")))
    }

    @Test
    fun findOneVersion_shouldReturnJson() {
        mockMvc.perform(get("/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/versions/v1")
                .header("Accept", "application/json")
                .with(user("user"))
                .with(csrf()))
                .andExpect(content().string("{\"version\":\"v1\",\"content\":\"{\\\"info\\\": {\\\"title\\\": \\\"Spec1\\\",  \\\"version\\\": \\\"v1\\\", \\\"description\\\": \\\"Description\\\"}}\"}"))
    }

    @Test
    fun findOneVersion_shouldReturnYaml() {
        mockMvc.perform(get("/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/versions/v1")
                .header("Accept", "application/yml")
                .with(user("user"))
                .with(csrf()))
                .andExpect(content().string("{\"version\":\"v1\",\"content\":\"---\\ninfo:\\n  title: \\\"Spec1\\\"\\n  version: \\\"v1\\\"\\n  description: \\\"Description\\\"\\n\"}"))
    }

    @Test
    fun deleteVersion_shouldDeleteVersion() {
        mockMvc.perform(delete("/api/1.0/specifications/f67cb0a6-c31b-424b-bfbb-ab0e163955ca/v2")
            .with(user("user"))
            .with(csrf()))
            .andExpect(status().isOk)
    }
}