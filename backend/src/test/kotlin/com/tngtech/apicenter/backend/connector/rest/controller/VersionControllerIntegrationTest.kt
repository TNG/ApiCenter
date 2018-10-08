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
        mockMvc.perform(get("/versions/ec8938e6-d5dd-4378-afbe-91b46e431e3c"))
            .andExpect(jsonPath("$.version", equalTo("v1")))
            .andExpect(jsonPath("$.id", equalTo("ec8938e6-d5dd-4378-afbe-91b46e431e3c")))
    }

    @Test
    fun deleteVersion_shouldDeleteVersion() {
        mockMvc.perform(delete("/versions/852676cf-c19d-4ab7-a176-0b019e0f9cb2"))
            .andExpect(status().isOk)
    }
}