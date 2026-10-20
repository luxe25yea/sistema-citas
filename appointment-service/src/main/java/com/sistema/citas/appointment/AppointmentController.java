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

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;

    public AppointmentController(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping
    public List<Appointment> findAll(@RequestParam(required = false) Long patientId) {
        if (patientId != null) {
            return appointmentRepository.findByPatientId(patientId);
        }
        return appointmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Appointment findById(@PathVariable Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una cita con id " + id));
    }

    @PostMapping
    public ResponseEntity<Appointment> create(@Valid @RequestBody CreateAppointmentRequest request) {
        // Regla de conflicto 1: El médico ya tiene cita en esa fecha y hora
        if (appointmentRepository.existsByDoctorNameAndDateTimeAndStatusNot(
                request.doctorName(),
                request.dateTime(),
                AppointmentStatus.CANCELLED)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto de agenda: El médico '" + request.doctorName()
                            + "' ya tiene una cita reservada en el horario " + request.dateTime());
        }

        // Regla de conflicto 2: El paciente ya tiene cita en esa misma fecha y hora
        if (appointmentRepository.existsByPatientIdAndDateTimeAndStatusNot(
                request.patientId(),
                request.dateTime(),
                AppointmentStatus.CANCELLED)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Conflicto de agenda: El paciente con id " + request.patientId()
                            + " ya tiene otra cita reservada en el horario " + request.dateTime());
        }

        Appointment appointment = new Appointment(
                request.patientId(),
                request.doctorName(),
                request.dateTime(),
                request.reason()
        );
        Appointment saved = appointmentRepository.save(appointment);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(location).body(saved);
    }
}
