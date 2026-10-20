package com.sistema.citas.patient;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Componente que ejecuta código al arrancar la aplicación (CommandLineRunner).
 * Se encarga de precargar datos de prueba en la base de datos H2 en memoria
 * para que el servicio tenga información lista para consultar y probar.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;

    public DataInitializer(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public void run(String... args) {
        // Evita duplicar registros si la base ya tiene datos
        if (patientRepository.count() > 0) {
            return;
        }

        // Carga de pacientes iniciales
        patientRepository.save(new Patient("Ana", "Gomez", "ana.gomez@ejemplo.com", "+54 11 5555 0101"));
        patientRepository.save(new Patient("Luis", "Fernandez", "luis.fernandez@ejemplo.com", "+54 11 5555 0202"));
        patientRepository.save(new Patient("Sofia", "Rios", "sofia.rios@ejemplo.com", "+54 11 5555 0303"));
    }
}
