package com.sistema.citas.appointment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para appointment-service.
 * Captura fallos de validación en los DTOs y construye respuestas legibles.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Intercepta errores de validación de campos provocados por anotaciones como @NotBlank, @NotNull, @Future.
     *
     * @param ex Excepción lanzada cuando los datos del request no superan las validaciones
     * @return Respuesta HTTP 400 con los campos y mensajes de error específicos
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // Recolecta el mensaje de error de cada campo inválido
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", "Error de validación en los campos enviados");
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }
}
