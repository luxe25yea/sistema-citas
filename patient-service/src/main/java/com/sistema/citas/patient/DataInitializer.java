package com.sistema.citas.patient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;

    public DataInitializer(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        if (patientRepository.count() > 0) {
            return;
        }
        patientRepository.save(new Patient("Ana", "Gomez", "ana.gomez@ejemplo.com", "+54 11 5555 0101"));
        patientRepository.save(new Patient("Luis", "Fernandez", "luis.fernandez@ejemplo.com", "+54 11 5555 0202"));
        patientRepository.save(new Patient("Sofia", "Rios", "sofia.rios@ejemplo.com", "+54 11 5555 0303"));
    }
}
