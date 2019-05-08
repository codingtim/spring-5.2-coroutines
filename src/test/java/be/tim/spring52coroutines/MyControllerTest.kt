package be.tim.spring52coroutines

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders

internal class MyControllerTest {

    private val myController = MyController()
    private val mockMvc = MockMvcBuilders.standaloneSetup(myController).build()
    private val webTestClient = WebTestClient.bindToController(myController).build()

    @Test
    internal fun callNormal() {
        mockMvc.get("/normal") {
            accept = APPLICATION_JSON
        }.andDo {
            print()
        }.andExpect {
            status { isOk }
            jsonPath("$.value") { value("normal") }
        }
    }

    @Test
    internal fun call() {
        mockMvc.get("/execute") {
            accept = APPLICATION_JSON
        }.andExpect {
            request {
                asyncStarted()
            }
        }
                .asyncDispatch(mockMvc)
                .andExpect {
                    status { isOk }
                    jsonPath("$.value") { value("test") }
                }
    }

    @Test
    internal fun callCo() {
        webTestClient.get().uri("/executeCo").exchange()
                .expectStatus().isOk
                .expectBody()
                .jsonPath("$.value").isEqualTo("testCo")
    }

    fun ResultActionsDsl.asyncDispatch(mockMvc: MockMvc): ResultActionsDsl {
        val mvcResult = andReturn()
        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult))
        return this
    }
}