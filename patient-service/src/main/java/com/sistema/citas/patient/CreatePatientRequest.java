package com.sistema.citas.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO (Data Transfer Object) inmutable que encapsula el cuerpo de la petición
 * para registrar un nuevo paciente vía POST /patients.
 *
 * Utiliza Bean Validation para asegurar que los datos requeridos lleguen completos
 * y con el formato adecuado antes de llegar a la lógica de negocio.
 */
public record CreatePatientRequest(

        // Valida que el nombre no sea nulo ni contenga únicamente espacios en blanco
        @NotBlank(message = "El nombre es obligatorio")
        String firstName,

        // Valida que el apellido no sea nulo ni contenga únicamente espacios en blanco
        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        // Valida que el email no esté vacío y cumpla con el estándar RFC de formato de correo
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El formato de email no es válido")
        String email,

        // Valida que el teléfono de contacto no esté vacío
        @NotBlank(message = "El teléfono es obligatorio")
        String phone
) {
}
