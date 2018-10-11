package com.tngtech.apicenter.backend.connector.rest.controller

import org.hamcrest.Matchers.equalTo
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
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
        mockMvc.perform(get("/specifications/b6b06513-d259-4faf-b34b-a216b3daad6a/versions/v1"))
            .andExpect(jsonPath("$.version", equalTo("v1")))
    }

    @Test
    fun deleteVersion_shouldDeleteVersion() {
        mockMvc.perform(delete("/specifications/af0502a2-7410-40e4-90fd-3504f67de1ee/versions/1.1"))
            .andExpect(status().isOk)
    }
}