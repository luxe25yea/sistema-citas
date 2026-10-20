package com.sistema.citas.appointment;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos de prueba para appointment-service.
 * Inserta citas de ejemplo al inicio si la base de datos se encuentra vacía.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final AppointmentRepository appointmentRepository;

    public DataInitializer(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        // Si ya hay citas precargadas, no vuelve a insertar
        if (appointmentRepository.count() > 0) {
            return;
        }

        // Cita 1: Dra. Maria Lopez con paciente 1
        appointmentRepository.save(new Appointment(
                1L,
                "Dra. Maria Lopez",
                LocalDateTime.of(2026, 11, 10, 10, 0),
                "Control general anual"
        ));

        // Cita 2: Dr. Roberto Gomez con paciente 2
        appointmentRepository.save(new Appointment(
                2L,
                "Dr. Roberto Gomez",
                LocalDateTime.of(2026, 11, 10, 11, 30),
                "Consulta dermatológica"
        ));

        // Cita 3: Dr. Roberto Gomez con paciente 1 en otra fecha
        appointmentRepository.save(new Appointment(
                1L,
                "Dr. Roberto Gomez",
                LocalDateTime.of(2026, 11, 12, 16, 0),
                "Seguimiento de tratamiento"
        ));
    }
}
