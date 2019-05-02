package be.tim.spring52coroutines

import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

internal class MyControllerTest {

    private val mockMvc = MockMvcBuilders.standaloneSetup(MyController()).build()

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
//            mockMvc.perform(asyncDispatch(mvc))
//            status { isOk }
//            jsonPath("$.value") { value("test") }
        /*  mockMvc.perform(asyncDispatch(mvcResult))
                  //.andExpect()
                  .andExpect(MockMvcResultMatchers.status().isOk)
                  .andExpect(MockMvcResultMatchers.jsonPath("$.value", Is.`is`("test")))*/
    }

    @Test
    internal fun callCo() {
        mockMvc.get("/executeCo") {
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

    fun ResultActionsDsl.asyncDispatch(mockMvc: MockMvc): ResultActionsDsl {
        val mvcResult = andReturn()
        mockMvc.perform(MockMvcRequestBuilders.asyncDispatch(mvcResult))
        return this
    }
}