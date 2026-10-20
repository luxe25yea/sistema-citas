package com.sistema.citas.patient;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    boolean existsByEmail(String email);
    Optional<Patient> findByEmail(String email);
}
