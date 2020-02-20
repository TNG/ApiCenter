package com.tngtech.apicenter.controller

import com.tngtech.apicenter.dto.ApplicationDto
import com.tngtech.apicenter.service.ApplicationService
import java.util.UUID
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import javax.persistence.EntityNotFoundException

@WebMvcTest(ApplicationController::class)
class ApplicationControllerUnitTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var applicationService: ApplicationService

    @Test
    fun `should return applications`() {
        val firstApplication = ApplicationDto(null, "firstApplication", "myFirstApplication", "myContact")
        val secondApplication = ApplicationDto(null, "secondApplication", "mySecondApplication", "otherContact")

        given(applicationService.getApplications()).willReturn(listOf(firstApplication, secondApplication))

        mockMvc.get("/api/applications").andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$[0].name") { value("firstApplication") }
            jsonPath("$[0].description") { value("myFirstApplication") }
            jsonPath("$[0].contact") { value("myContact") }
            jsonPath("$[1].name") { value("secondApplication") }
            jsonPath("$[1].description") { value("mySecondApplication") }
            jsonPath("$[1].contact") { value("otherContact") }
        }

        verify(applicationService).getApplications()
    }

    @Test
    fun `should create application`() {
        val applicationToCreate = ApplicationDto(null, "applicationToCreate", "thisIsTheApplicationToBeCreated", "myContact")

        given(applicationService.createApplication(applicationToCreate)).willReturn(applicationToCreate)

        mockMvc.post("/api/applications") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationToCreate",
                    "description": "thisIsTheApplicationToBeCreated",
                    "contact": "myContact"
                }
            """.trimIndent()
        }.andExpect {
            status { isCreated }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationToCreate") }
            jsonPath("$.description") { value("thisIsTheApplicationToBeCreated") }
            jsonPath("$.contact") { value("myContact") }
        }

        verify(applicationService).createApplication(applicationToCreate)
    }

    @Test
    fun `should update application`() {
        val applicationIdToUpdate = UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673")

        val applicationUpdate = ApplicationDto(null, "applicationNameAfter", "applicationDescriptionAfter", "contactAfter")

        given(applicationService.updateApplication(applicationIdToUpdate, applicationUpdate)).willReturn(applicationUpdate)

        mockMvc.put("/api/applications/7da9b86a-9322-425a-b2db-1bcf692ab673") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                {
                    "name": "applicationNameAfter",
                    "description": "applicationDescriptionAfter",
                    "contact": "contactAfter"
                }
            """.trimIndent()
        }.andExpect {
            status { isOk }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") { value("applicationNameAfter") }
            jsonPath("$.description") { value("applicationDescriptionAfter") }
            jsonPath("$.contact") { value("contactAfter") }
        }

        verify(applicationService).updateApplication(applicationIdToUpdate, applicationUpdate)
    }

    @Test
    fun `should delete application`() {
        val applicationIdToDelete = UUID.fromString("7da9b86a-9322-425a-b2db-1bcf692ab673")

        mockMvc.delete("/api/applications/7da9b86a-9322-425a-b2db-1bcf692ab673").andExpect {
            status { isNoContent }
        }

        verify(applicationService).deleteApplication(applicationIdToDelete)
    }
}
