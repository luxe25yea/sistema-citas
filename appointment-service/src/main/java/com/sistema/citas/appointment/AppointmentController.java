package com.sistema.citas.appointment;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

/**
 * Controlador REST que expone los endpoints para la reserva y gestión de citas médicas.
 * Base path: /appointments
 */
@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    // Inyección de dependencias por constructor
    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Endpoint GET /appointments
     * Lista todas las citas médicas registradas.
     * Opcionalmente permite filtrar por paciente si se proporciona el parámetro ?patientId={id}.
     *
     * @param patientId ID del paciente (opcional)
     * @return Lista de citas
     */
    @GetMapping
    public List<Appointment> findAll(@RequestParam(required = false) Long patientId) {
        if (patientId != null) {
            return appointmentRepository.findByPatientId(patientId);
        }
        return appointmentRepository.findAll();
    }

    /**
     * Endpoint GET /appointments/{id}
     * Busca y retorna el detalle de una cita por su ID.
     * Si no existe, retorna HTTP 404 Not Found.
     *
     * @param id Identificador de la cita
     * @return La cita encontrada
     */
    @GetMapping("/{id}")
    public Appointment findById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una cita con id " + id));
    }

    /**
     * Endpoint POST /appointments
     * Reserva una nueva cita médica aplicando las reglas de conflicto de agenda.
     *
     * Regla 1: El médico no debe tener otra cita confirmada en la misma fecha y hora.
     * Regla 2: El paciente no debe tener otra cita confirmada en la misma fecha y hora.
     *
     * @param request DTO con los datos de la cita validado mediante @Valid
     * @return HTTP 201 Created con cabecera Location hacia /appointments/{id} y el cuerpo con la cita creada
     */
    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody CreateAppointmentRequest request) {
        // Regla de conflicto 1: Verifica si el médico ya está ocupado en ese horario
        if (appointmentRepository.existsByDoctorNameAndDateTimeAndStatusNot(
                request.doctorName(),
                request.dateTime(),
                AppointmentStatus.CANCELLED)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto de agenda: El médico '" + request.doctorName()
                            + "' ya tiene una cita reservada en el horario " + request.dateTime());
        }

        // Regla de conflicto 2: Verifica si el paciente ya tiene otra cita en ese mismo horario
        if (appointmentRepository.existsByPatientIdAndDateTimeAndStatusNot(
                request.patientId(),
                request.dateTime(),
                AppointmentStatus.CANCELLED)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto de agenda: El paciente con id " + request.patientId()
                            + " ya tiene otra cita reservada en el horario " + request.dateTime());
        }

        // Creación y persistencia de la nueva cita médica
        Appointment appointment = new Appointment(
                request.patientId(),
                request.doctorName(),
                request.dateTime(),
                request.reason()
        );
        Appointment saved = appointmentRepository.save(appointment);

        // Construcción de la URI del nuevo recurso creado (/appointments/{id})
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        // Retorna HTTP 201 Created con el header Location
        return ResponseEntity.created(location).body(saved);
    }
}
