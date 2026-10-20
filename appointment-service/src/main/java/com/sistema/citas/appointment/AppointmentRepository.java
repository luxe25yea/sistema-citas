package com.sistema.citas.appointment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio Spring Data JPA para la entidad Appointment.
 * Incluye consultas derivadas para recuperación y detección de solapamiento de turnos.
 */
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Recupera todas las citas asociadas a un determinado paciente.
     *
     * @param patientId ID del paciente
     * @return Lista de citas del paciente
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Recupera todas las citas asignadas a un médico determinado.
     *
     * @param doctorName Nombre del médico
     * @return Lista de citas asignadas al médico
     */
    List<Appointment> findByDoctorName(String doctorName);

    /**
     * Regla de conflicto del médico:
     * Verifica si el médico ya tiene una cita agendada en la fecha y hora indicadas
     * cuyo estado sea diferente a un estado dado (generalmente diferente a CANCELLED).
     *
     * @param doctorName Nombre del médico
     * @param dateTime Fecha y hora del turno a verificar
     * @param status Estado a excluir (CANCELLED)
     * @return true si el médico ya tiene una cita activa en ese horario
     */
    boolean existsByDoctorNameAndDateTimeAndStatusNot(
            String doctorName,
            LocalDateTime dateTime,
            AppointmentStatus status
    );

    /**
     * Regla de conflicto del paciente:
     * Verifica si el paciente ya tiene otra cita agendada en la misma fecha y hora
     * cuyo estado sea diferente a CANCELLED.
     *
     * @param patientId ID del paciente
     * @param dateTime Fecha y hora del turno a verificar
     * @param status Estado a excluir (CANCELLED)
     * @return true si el paciente ya tiene una cita activa en ese horario
     */
    boolean existsByPatientIdAndDateTimeAndStatusNot(
            Long patientId,
            LocalDateTime dateTime,
            AppointmentStatus status
    );
}
