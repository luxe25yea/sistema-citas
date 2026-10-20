package com.sistema.citas.patient;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para la entidad Patient.
 * Proporciona automáticamente métodos CRUD (save, findById, findAll, delete, etc.)
 * y permite definir consultas derivadas por el nombre del método.
 */
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * Verifica si ya existe un paciente registrado con el correo electrónico dado.
     * Utilizado para validar unicidad y evitar duplicados (HTTP 409 Conflict).
     *
     * @param email Correo electrónico a consultar
     * @return true si ya existe un paciente con ese correo, false de lo contrario
     */
    boolean existsByEmail(String email);

    /**
     * Busca un paciente por su correo electrónico.
     *
     * @param email Correo a buscar
     * @return Optional con el paciente encontrado, o vacío si no existe
     */
    Optional<Patient> findByEmail(String email);
}
