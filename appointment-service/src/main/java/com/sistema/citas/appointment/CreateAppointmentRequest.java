package com.sistema.citas.appointment;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO (Data Transfer Object) inmutable para recibir los datos al crear una cita (POST /appointments).
 * Aplica validaciones de Bean Validation antes de procesar la solicitud.
 */
public record CreateAppointmentRequest(

        // El ID del paciente es obligatorio y debe existir
        @NotNull(message = "El id del paciente es obligatorio")
        Long patientId,

        // Nombre del médico asignado a la cita
        @NotBlank(message = "El nombre del médico es obligatorio")
        String doctorName,

        // La fecha y hora de la cita no puede ser nula y debe estar en el futuro
        @NotNull(message = "La fecha y hora de la cita es obligatoria")
        @Future(message = "La fecha y hora de la cita debe ser posterior al momento actual")
        LocalDateTime dateTime,

        // Motivo descriptivo de la consulta
        @NotBlank(message = "El motivo de la cita es obligatorio")
        String reason
) {
}
