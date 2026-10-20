package com.sistema.citas.appointment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldFindAllAppointments() throws Exception {
        mockMvc.perform(get("/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    void shouldFindAppointmentById() throws Exception {
        mockMvc.perform(get("/appointments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.doctorName", is("Dra. Maria Lopez")));
    }

    @Test
    void shouldFilterAppointmentsByPatientId() throws Exception {
        mockMvc.perform(get("/appointments?patientId=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].patientId", is(1)));
    }

    @Test
    void shouldReturn404WhenAppointmentNotFound() throws Exception {
        mockMvc.perform(get("/appointments/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAppointmentSuccessfully() throws Exception {
        String newAppointmentJson = """
                {
                    "patientId": 3,
                    "doctorName": "Dra. Laura Sanchez",
                    "dateTime": "2026-11-20T09:00:00",
                    "reason": "Consulta oftalmológica"
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newAppointmentJson))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.patientId", is(3)))
                .andExpect(jsonPath("$.doctorName", is("Dra. Laura Sanchez")))
                .andExpect(jsonPath("$.status", is("CONFIRMED")));
    }

    @Test
    void shouldReturn409WhenDoctorHasConflict() throws Exception {
        // En DataInitializer: "Dra. Maria Lopez" ya tiene cita el "2026-11-10T10:00:00"
        String conflictJson = """
                {
                    "patientId": 3,
                    "doctorName": "Dra. Maria Lopez",
                    "dateTime": "2026-11-10T10:00:00",
                    "reason": "Intento de cita en horario ocupado del médico"
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictJson))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn409WhenPatientHasConflict() throws Exception {
        // En DataInitializer: patientId 1 ya tiene cita el "2026-11-10T10:00:00"
        String conflictJson = """
                {
                    "patientId": 1,
                    "doctorName": "Dr. Otro Medico",
                    "dateTime": "2026-11-10T10:00:00",
                    "reason": "Intento de cita en horario ocupado del paciente"
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(conflictJson))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenValidationFails() throws Exception {
        String invalidJson = """
                {
                    "patientId": null,
                    "doctorName": "",
                    "dateTime": "2020-01-01T10:00:00",
                    "reason": ""
                }
                """;

        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.patientId", notNullValue()))
                .andExpect(jsonPath("$.errors.doctorName", notNullValue()))
                .andExpect(jsonPath("$.errors.dateTime", notNullValue()))
                .andExpect(jsonPath("$.errors.reason", notNullValue()));
    }
}
