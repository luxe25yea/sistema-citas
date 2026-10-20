package com.sistema.citas.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateAppointmentRequest(
        @NotNull(message = "El id del paciente es obligatorio")
        Long patientId,

        @NotBlank(message = "El nombre del médico es obligatorio")
        String doctorName,

        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser posterior al momento actual")
        LocalDateTime dateTime,

        @NotBlank(message = "El motivo de la cita es obligatorio")
        String reason
) {
}
