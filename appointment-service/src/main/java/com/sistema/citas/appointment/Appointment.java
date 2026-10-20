package com.sistema.citas.appointment;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidad JPA que representa una cita médica.
 * Mapea directamente con la tabla 'appointments' en la base de datos propia de appointment-service.
 */
@Entity
@Table(name = "appointments")
public class Appointment {

    // Identificador único de la cita médica (clave primaria generada por H2)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referencia lógica al identificador del paciente en patient-service (clave foránea entre microservicios)
    private Long patientId;

    // Nombre o identificador del profesional médico que atenderá la cita
    private String doctorName;

    // Fecha y hora programada para la cita
    private LocalDateTime dateTime;

    // Motivo o descripción clínica de la consulta
    private String reason;

    // Estado actual de la cita (CONFIRMED, CANCELLED) persistido como texto en la base de datos
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    /**
     * Constructor por defecto protegido exigido por la especificación JPA.
     */
    protected Appointment() {
    }

    /**
     * Constructor para agendar una nueva cita.
     * El estado inicial por defecto se establece en CONFIRMED.
     */
    public Appointment(Long patientId, String doctorName, LocalDateTime dateTime, String reason) {
        this.patientId = patientId;
        this.doctorName = doctorName;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = AppointmentStatus.CONFIRMED;
    }

    public Long getId() {
        return id;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
