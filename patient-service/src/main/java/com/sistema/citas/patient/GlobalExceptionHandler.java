package com.sistema.citas.patient;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Controlador de excepciones global (@RestControllerAdvice) para interceptar
 * y formatear errores de forma estandarizada en respuestas JSON legibles.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta errores de Bean Validation cuando falla la anotación @Valid en un @RequestBody.
     * Transforma la excepción técnica en un mapa claro con:
     * - status: 400
     * - error: Bad Request
     * - message: Resumen del error
     * - errors: Mapa con clave del campo y mensaje específico de validación
     *
     * @param ex Excepción lanzada por Spring MVC al fallar la validación
     * @return ResponseEntity con HTTP 400 y detalle de campos inválidos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Itera sobre todos los campos que no superaron las restricciones de validación
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        // Construcción del payload de respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Error de validación en los campos enviados");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }
}
