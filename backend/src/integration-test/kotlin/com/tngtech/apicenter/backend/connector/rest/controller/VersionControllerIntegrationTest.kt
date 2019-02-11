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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class VersionControllerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun findOneVersion_shouldReturnVersion() {
        mockMvc.perform(get("/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/versions/v1")
            .with(user("user"))
            .with(csrf()))
            .andExpect(jsonPath("$.version", equalTo("v1")))
    }

    @Test
    fun findOneVersion_shouldReturnVersionWithAcceptHeader() {
        mockMvc.perform(get("/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/versions/v1")
                .header("Accept", "application/json")
                .with(user("user"))
                .with(csrf()))
                .andExpect(jsonPath("$.version", equalTo("v1")))
    }

    @Test
    fun deleteVersion_shouldDeleteVersion() {
        mockMvc.perform(delete("/specifications/f67cb0a6-c31b-424b-bfbb-ab0e163955ca/versions/v2")
            .with(user("user"))
            .with(csrf()))
            .andExpect(status().isOk)
    }
}