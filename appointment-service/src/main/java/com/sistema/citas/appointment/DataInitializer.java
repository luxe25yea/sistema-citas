package com.sistema.citas.appointment;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AppointmentRepository appointmentRepository;

    public DataInitializer(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public void run(String... args) {
        if (appointmentRepository.count() > 0) {
            return;
        }

        appointmentRepository.save(new Appointment(
                1L,
                "Dra. Maria Lopez",
                LocalDateTime.of(2026, 11, 10, 10, 0),
                "Control general anual"
        ));

        appointmentRepository.save(new Appointment(
                2L,
                "Dr. Roberto Gomez",
                LocalDateTime.of(2026, 11, 10, 11, 30),
                "Consulta dermatológica"
        ));

        appointmentRepository.save(new Appointment(
                1L,
                "Dr. Roberto Gomez",
                LocalDateTime.of(2026, 11, 12, 16, 0),
                "Seguimiento de tratamiento"
        ));
    }
}
