package com.sistema.citas.patient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindAllPatients() throws Exception {
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void shouldFindPatientById() throws Exception {
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Ana")));
    }

    @Test
    void shouldReturn404WhenPatientNotFound() throws Exception {
        mockMvc.perform(get("/patients/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreatePatientSuccessfully() throws Exception {
        String newPatientJson = """
                {
                    "firstName": "Carlos",
                    "lastName": "Mendoza",
                    "email": "carlos.mendoza@ejemplo.com",
                    "phone": "+54 11 5555 0404"
                }
                """;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newPatientJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("Mendoza")))
                .andExpect(jsonPath("$.email", is("carlos.mendoza@ejemplo.com")));
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        String invalidPatientJson = """
                {
                    "firstName": "",
                    "lastName": "Mendoza",
                    "email": "no-es-un-email",
                    "phone": ""
                }
                """;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPatientJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.firstName", notNullValue()))
                .andExpect(jsonPath("$.errors.email", notNullValue()))
                .andExpect(jsonPath("$.errors.phone", notNullValue()));
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() throws Exception {
        String duplicatePatientJson = """
                {
                    "firstName": "Ana",
                    "lastName": "Otra",
                    "email": "ana.gomez@ejemplo.com",
                    "phone": "+54 11 9999 9999"
                }
                """;

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicatePatientJson))
                .andExpect(status().isConflict());
    }
}
