package com.sistema.citas.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePatientRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        String phone
) {
}
